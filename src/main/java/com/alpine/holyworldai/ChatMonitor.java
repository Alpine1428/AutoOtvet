package com.alpine.holyworldai;

import net.minecraft.client.MinecraftClient;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMonitor {

    private static final Pattern COLOR =
            Pattern.compile("§.");

    private static final Pattern CHECK =
            Pattern.compile("\\[CHECK\\]\\s+(\\S+)\\s+->\\s+(.+)");

    private final Random random = new Random();

    public void onChatMessage(String fullMessage) {

        if (!HolyWorldAIClient.enabled) return;

        // Убираем цвет-коды
        String clean = COLOR.matcher(fullMessage).replaceAll("");

        // DEBUG (можешь удалить потом)
        System.out.println("CHAT CLEAN: " + clean);

        Matcher matcher = CHECK.matcher(clean);
        if (!matcher.find()) return;

        String message = matcher.group(2).toLowerCase().trim();

        String response = generate(message);

        if (response == null) return;

        MinecraftClient.getInstance().execute(() ->
                MinecraftClient.getInstance().player
                        .networkHandler.sendChatMessage(response)
        );
    }

    private String generate(String msg) {

        if (contains(msg, "за что", "почему", "причина"))
            return r("Проверка по репортам.",
                     "Многочисленные жалобы.",
                     "Подозрительная активность.");

        if (contains(msg, "я не читер", "я чист", "без читов"))
            return r("Сейчас проверим.",
                     "Если чист — проблем не будет.",
                     "Скачивай AnyDesk.");

        if (contains(msg, "дс", "discord"))
            return r("Discord не используем.",
                     "Проверка только через удалённый доступ.",
                     "Нет.");

        if (contains(msg, "не качается", "нет ани", "не работает"))
            return r("Скачивай с официального сайта.",
                     "Попробуй RustDesk.",
                     "Используй RustDesk.");

        if (contains(msg, "сколько", "время", "минут"))
            return r("3 минуты.",
                     "2 минуты.",
                     "Время идёт.");

        if (contains(msg, "признаю", "я софт", "бань"))
            return r("Фиксирую признание.",
                     "Признание уменьшает срок.",
                     "Оформляю.");

        return r("Жду.", "+", "AnyDesk.");
    }

    private boolean contains(String msg, String... arr) {
        for (String s : arr)
            if (msg.contains(s)) return true;
        return false;
    }

    private String r(String... arr) {
        return arr[random.nextInt(arr.length)];
    }
}
