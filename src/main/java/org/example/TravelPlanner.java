import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TravelPlanner {

    static class Stop {
        String name;
        Duration duration;
        ZoneId zone;

        public Stop(String name, Duration duration, ZoneId zone) {
            this.name = name;
            this.duration = duration;
            this.zone = zone;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть початковий пункт маршруту:");
        String startPoint = scanner.nextLine();

        System.out.println("Введіть кінцевий пункт маршруту:");
        String endPoint = scanner.nextLine();

        System.out.println("Введіть кількість зупинок:");
        int stopsCount = scanner.nextInt();
        scanner.nextLine();

        List<Stop> stops = new ArrayList<>();
        for (int i = 0; i < stopsCount; i++) {
            System.out.println("Назва зупинки " + (i + 1) + ":");
            String stopName = scanner.nextLine();

            System.out.println("Тривалість зупинки (години):");
            int hours = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Часовий пояс (наприклад, Europe/Kyiv):");
            String zoneId = scanner.nextLine();

            stops.add(new Stop(stopName, Duration.ofHours(hours), ZoneId.of(zoneId)));
        }

        System.out.println("Виберіть вид транспорту (1 - автомобіль, 2 - поїзд, 3 - літак):");
        int transportType = scanner.nextInt();
        scanner.nextLine();

        double speed = 0;
        double fuelConsumption = 0;
        switch (transportType) {
            case 1 -> {
                speed = 80; // Автомобіль, км/год
                fuelConsumption = 8; // Літрів на 100 км
            }
            case 2 -> {
                speed = 100; // Поїзд, км/год
                fuelConsumption = 0; // Паливо не враховується
            }
            case 3 -> {
                speed = 800; // Літак, км/год
                fuelConsumption = 30; // Літрів на 100 км
            }
            default -> throw new IllegalArgumentException("Невірний вибір транспорту");
        }

        System.out.println("Введіть відстань між пунктами маршруту (км):");
        double distance = scanner.nextDouble();

        System.out.println("Введіть бажану кількість годин для подорожі:");
        double maxHours = scanner.nextDouble();

        // Розрахунок
        double travelTimeWithoutStops = distance / speed;
        double totalFuelConsumption = (distance / 100) * fuelConsumption;

        double totalTravelTime = travelTimeWithoutStops;
        for (Stop stop : stops) {
            totalTravelTime += stop.duration.toHours();
        }

        if (totalTravelTime > maxHours) {
            System.out.println("Подорож перевищує обмеження часу.");
        } else {
            System.out.println("Час подорожі в межах обмеження.");
        }

        // Формат для дати та часу
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");

        // Виведення результатів
        System.out.println("\nПлан маршруту:");
        LocalDateTime departureTime = LocalDateTime.now();
        System.out.println("Відправлення з " + startPoint + " о " + departureTime.format(formatter));

        for (Stop stop : stops) {
            departureTime = departureTime.plus(stop.duration);
            System.out.println("Зупинка: " + stop.name + ", прибуття о " + departureTime.format(formatter));
        }

        departureTime = departureTime.plusHours((long) travelTimeWithoutStops);
        System.out.println("Прибуття в " + endPoint + " о " + departureTime.format(formatter));
        System.out.printf("Загальна тривалість подорожі: %.2f годин.\n", totalTravelTime);
        if (fuelConsumption > 0) {
            System.out.printf("Загальне споживання пального: %.2f літрів.\n", totalFuelConsumption);
        }
    }
}
