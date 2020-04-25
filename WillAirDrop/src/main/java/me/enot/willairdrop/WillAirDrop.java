package me.enot.willairdrop;

import me.enot.willairdrop.commands.AirDropCommand;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.events.DropEvents;
import me.enot.willairdrop.events.utils.Placeholder;
import me.enot.willairdrop.logic.utils.AirDropLogick;
import me.enot.willairdrop.logic.utils.Saver;
import me.enot.willairdrop.serializer.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class WillAirDrop extends JavaPlugin {

    public static Plugin getPlugin(){
        return WillAirDrop.getPlugin(WillAirDrop.class);
    }


    @Override
    public void onEnable() {

        PluginManager pm = Bukkit.getPluginManager();
        if(!pm.isPluginEnabled("HolographicDisplays") || !pm.isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("HolographicDisplays или PlaceholderAPI не найден");
            this.setEnabled(false);
            return;
        }
        Settings.reload();
        Language.reload();

        Serialize.load();
        new Placeholder(this).register();

        pm.registerEvents(new DropEvents(), this);

        getCommand("airdrop").setExecutor(new AirDropCommand());

        AirDropLogick.formatedTimeList();
        AirDropLogick.getList().forEach(string -> Bukkit.getConsoleSender().sendMessage(string));
        AirDropLogick.startTimer();

        Saver.load();
     }

    @Override
    public void onDisable() {
        Saver.save();
    }

    // TODO: 16.03.2020 tz Плагин - WillAirDrop (Название - Обязательно)
    //  Суть - Сбрасывает в определенное время "груз" (сундук) с каким либо лутом.
    //  Условно - Груз = Сундук
    //  Основное:
    //  +  1) 2 конфига (Основной - с настройками, дополнительный - с сообщениями)
    //  +  2) Возможность редактировать лут, который будет находится в сундуке (Указывать ID, кол-во)
    //    +- Можно делать НЕОГРАНИЧЕННОЕ кол-во вариантов лута (То есть, в конфиге можно будет сделать N списков с ID и кол-во)
    //    +- Каждому списку, можно дать собственное ИМЯ
    //    +- Возможность добавления зачарований на какие либо вещи
    //  +  3) КД для открытия сундука
    //    +- Время КД можно редактировать в конфиге
    //    +- КД для каждого игрока собственное
    //    +- Отсчет идет НАД сундуком (Игрок, кликая ПКМ по сундуку - вызывает галограмму с отчетом над сундуком, если игрока ударят, убьют, либо как то будут контактировать - отсчет
    //  обнуляется и начинается сначала (Второй раз нажимать ПКМ - НЕ нужно, если игрока убили - отсчет будет полностью остановлен и уже будет НУЖНО нажимать ПКМ для продолжения)
    //    +- Сообщение которое будет отображать галограмма, можно редактировать в конфиге
    //    +- Открыть сундук может сразу НЕОГРАНИЧЕННОЕ кол-во людей, если кто то открыл раньше других - сундук автоматически открывается для ВСЕХ
    //  4) Анимация на падение сундука (Плавно опускается с 256 высоты, пока не упадет на какой либо блок)
    //  - Если сундук еще не закончил падение, начать его открытие - НЕЛЬЗЯ
    //  +  5) Можно изменить вид Груза (Заменить условный сундук, на какой либо блок)
    //  +  6) Можно редактировать размеры карты в конфиге (X;Y;Z), по которой будет происходить выпадение груза
    //  +  7) Запретить падение сундука на:
    //    +1) Лава
    //    +2) Вода
    //  +  8) Время выпадения
    //    +- Время берется с машины на которой установлен сервер
    //    +- В конфиге мы указываем время задержки и время падения груза
    //    +- Задержка - время до падения, чтобы люди могли добежать
    //    +- Время падения - указываем в часах (ПРИМЕР: 10:00, ЗАДЕРЖКА: 10 (минут), задержка работает так:
    //  9:50 - происходит объявление в чате о том, что груз упадет через NN времени, на NN;NN;NN координатах, 10:00 - груз начинает падать
    //  +  9) Команда /airdrop start <time> <coord> (Вызывает дроп в не зависимости от указаного времени в конфиге, если администратор вызовет груз, время в конфиге НЕ сбрасывается)
    //    +- Время указывается в секундах (Через сколько упадет груз, допустим пишем /airdrop start 60 и через 60 секунд груз начинает падать)
    //    +- В чат выводится сообщение о том, что через NN секунд на NN;NN;NN координатах упадет груз (Дописывается, что вызвано администратором)
    //    +- Сообщение можно редактировать в конфиге
    //    +- На данную команду - пункт 11 НЕ действует, команда будет исполняться даже, если на сервере 1 игрок
    //  +  10) Выпадение груза
    //    +- Происходит по времени указаному в конфиге
    //    +- После того как прошло объявление о выпадение груза, есть NN времени, чтобы добежать до объявленных координат
    //    +- Время можно редактировать в конфиге
    //  (В пункте 8 все подробно прописанно)
    //    +- Каждый час выбирает РАНДОМНЫЙ конфиг с лутом груза и исполняется
    //    +- Не должен повторяться подряд, то есть, если в 11:00 выпал "drop1", в 12:00 "drop1" - выпасть НЕ может
    //  +  11) Учет онлайна
    //    +- Если на сервере находится меньше Х игроков, груз НЕ выпадает
    //    +- Кол-во игроков можно будет изменять в конфиге
    //    +- Опишу: Если в 11:00 на сервере было меньше Х игроков - груз НЕ упал, следующий груз будет т лько через 1 час (в 12:00)
    //    +- Добавить сообщение - "Груз отменен из-за недосточного кол-во игроков"
    //    +- Сообщение можно редактировать в конфиге
    //  +  Защита сундука (груза):
    //    +- Сундук НЕЛЬЗЯ взорвать ТНТ, Вагонеткой с ТНТ, Визером, Крипером (Использую яйцо крипера/приведя крипера к грузу)
    //    +- Также НЕЛЬЗЯ сломать сундук
    //  фиксы:
    //  1) фикс местоположения голограмм и спрятать голограмму "Груз" если начинается кд
    //  2) сохранение активных грузов и прогрузка при включении плагина


}