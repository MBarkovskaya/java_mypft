package java_mypft.soap;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class GeoIPServiceTests {

  @Test
  public void testMyIP() {
    //создаем объект типа GeoIPService, в нем выбираем тип реализации,
    //обращаемся через удаленный программный интерфейс к удаленному сервису, вызывая функцию getGeoIP, чтобы получить в ответе CountryCode
    GeoIP geoIP = new GeoIPService().getGeoIPServiceSoap12().getGeoIP("109.165.30.30");
    //проверяем соответствует ли полученный CountryCode ожидаемому значению RUS
    assertEquals(geoIP.getCountryCode(), "RUS");
  }

  @Test
  public void testInvalidIP() {
    GeoIP geoIP = new GeoIPService().getGeoIPServiceSoap12().getGeoIP("109.165.30.xxx");
    assertEquals(geoIP.getCountryCode(), "RUS");
  }
}
