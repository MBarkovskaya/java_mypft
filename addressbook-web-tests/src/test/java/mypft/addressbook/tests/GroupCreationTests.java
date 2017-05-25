package mypft.addressbook.tests;

import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

//библиотека hamcrest предоставляет fluent interface для написания проверок (вызван статический импорт методов equalTo и assertThat)

public class GroupCreationTests extends TestBase {

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testGroupCreation(GroupData group) {
    appLocal.get().goTo().GroupPage();
    Groups before = appLocal.get().db().groups();
    appLocal.get().group().create(group);
    //хэширование - предварительная проверка при помощи более быстрой операции appLocal.get().group().count()
    //прежде, чем загружать количество групп нужно проверить - сходится ли количество
    //если не сходится, то загружать его нет ни какого смысла
    assertThat(appLocal.get().group().count(), equalTo(before.size() + 1));
    Groups after = appLocal.get().db().groups();
    //проверяем, что множество after равно множеству before с добавленной группой - проверка, в которой используется hurmcrest - (MatcherAssert.assertThat()
    // after, equalTo(здесь нужно нажать ctrl+пробел 2 раза, чтобы подтянулись названия методов, а не только классов)
    //метод equalTo находится в классе CoreMatchers - это проверялка, некторые из проверялок нужно подключать как отдельные самостоятельные библиотеки
    //новой добавленной группе присваивается идентификатор group.withId(здесь должен вычисляться максимальный идентификатор)
    //первый способ:
    //берем колекцию after, которая содержит группы с уже известными идентификаторами, превращаем ее в поток
    //в этом потоке ищем максимальный элемент при помощи Comparator, который сравнивает объекты друг с другом
    //(используем Comparator, который сравнивает группы по их идентификаторам)
    //второй способ:
    //поток объектов типа GroupData превращаем в поток идентификаторов с помощью функции mapToInt
    //которая в качестве параметра принимает описание того, как объект преобразуется в число
    //т.е. в качестве параментра передаем анонимную функцию, которая будет последовательно применяться ко всем элементам потока
    // и каждый из них будет последовательно преобразовываться в число
    //в результате, мы из потока объектов GroupData получаем поток целых чисел
    //анонимная функция, в которой в качестве параметра mapToInt принимает группу g,  результат этой функции - идентификатор этой группы  g.getId()
    assertThat(after, equalTo(
            before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    //добавить метод withAdded в уже существующий интерфейс Set - нельзя, и даже в уже существующий класс нельзя, поэтому
    //создаем свой собственный класс Groups, который ведет себя как Set, но в него можно добавить свои собственные методы
  }

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testBadGroupCreation() {
    appLocal.get().goTo().GroupPage();
    Groups before = appLocal.get().db().groups();
    GroupData group = new GroupData().withName("test2'");
    appLocal.get().group().create(group);
    assertThat(appLocal.get().group().count(), equalTo(before.size()));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before));
    verifyGroupListInUI();
  }

}
