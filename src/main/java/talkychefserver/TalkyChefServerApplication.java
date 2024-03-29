package talkychefserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO в бд холостое увеличение ID в сиквенсах происходит при неудачной попытке вставить значения
// TODO тригеров сделать для дропа ненужных картинок, сетов единиц и т.д.
// TODO проверить генерацию последовательностей для каждой таблицы
// TODO cross-origin настроить только на сайт. Сейчас всё разрешил.
// TODO в названиях ингредиентов/единиц измерений запретить спецсимволы и цифры
// TODO на каждый сценарий добавления тестик бы бахнуть
// TODO manytomany нужно на set-ах делать. Читай подробнее.
// TODO эксепшены вообще не обрабатываются нормально, обычно тупо 500 отсылает
// TODO логирование сделать
// TODO многопоточность
@SpringBootApplication
public class TalkyChefServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkyChefServerApplication.class, args);
    }

}

