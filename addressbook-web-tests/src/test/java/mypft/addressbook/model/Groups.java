package mypft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.security.acl.Group;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//для реализации класса Groups воспользуемся библиотекой Guava, в которой есть набор вспомогательгных
// классов, которые предназначены для построения коллекций с расширенным набором методов
public class Groups extends ForwardingSet<GroupData> {

  //1 сначала создаем объект, которому это будет делегироваться
    private Set<GroupData> delegate;

    //4 создаем конструктор (берем множество из существующего объекта groups, который передан в качестве параметра groups.delegate
  //строим новое множество new HashSet<GroupData>, которое содержит те же самые элементы и присваиваем это новое множество в качестве атрибута
  //в новый, создаваемый этим конструктором объект this.delegate
    public Groups(Groups groups) {
        this.delegate = new HashSet<GroupData>(groups.delegate);
    }

    public Groups() {
        this.delegate = new HashSet<GroupData>();
    }

  public Groups(Collection<GroupData> groups) {
      //строим новый HashSet, т.е. множество объектов типа GroupData из коллекции
    //копируем, чтобы никто не испортил
    this.delegate = new HashSet<GroupData>(groups);
  }

  //2 обязательный метод класса ForwardingSet - delegate()
  //метод должен возвращаеть созданный объект delegate (то, что названия совпадают - не имеет значения)
  @Override
    protected Set<GroupData> delegate() {
        return delegate;
    }

    //3 добавляем свой собственный метод
    public Groups withAdded(GroupData group) {
      //чтобы можно было вытягивать методы в цепочки и строить из них каскады, должен возвращаться объект типа Groups
      //будем делать копию, чтобы старый объект остался неизменным,  а метод  withAdded возвращает новый объект с добавленной новой группой
     Groups groups = new Groups(this);
     //5 добавляем объект, который передан в качестве параметра
      //метод add реализован в классе ForwardingSet
     groups.add(group);
     //возвращаем построенную копию с добавленной группой
     return groups;
    }

    //метод возвращает копию объекта типа Groups, из которой удалена какая-то группа
    public Groups without(GroupData group) {
        Groups groups = new Groups(this);
        groups.remove(group);
        return groups;
    }
}
