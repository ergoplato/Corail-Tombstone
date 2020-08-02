package ovh.corail.tombstone.helper;

import net.minecraft.world.World;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public class TimeHelper {

    public static long systemTime() {
        return System.currentTimeMillis();
    }

    public static long systemTicks() {
        return System.currentTimeMillis() / 50;
    }

    public static boolean isSystemTimeElapsed(long oldTime, long millis) {
        return systemTime() - oldTime >= millis;
    }

    public static long worldTicks(World world) {
        return world.getGameTime();
    }

    public static int tickFromSecond(int second) {
        return second * 20;
    }

    public static int tickFromMinute(int minute) {
        return minute * 1200;
    }

    public static int tickFromHour(int hour) {
        return hour * 72000;
    }

    public static int tickFromDay(int day) {
        return day * 1728000;
    }

    public static boolean atInterval(long ticksExisted, int tick) {
        return testInterval(ticksExisted, tick, interval -> interval == 0);
    }

    public static boolean inIntervalBefore(long ticksExisted, int tick, int intervalStart) {
        return testInterval(ticksExisted, tick, interval -> interval > intervalStart);
    }

    public static boolean inIntervalAfter(long ticksExisted, int tick, int intervalStart) {
        return testInterval(ticksExisted, tick, interval -> interval < intervalStart);
    }

    public static boolean testInterval(long ticksExisted, int tick, Predicate<Long> intervalPredic) {
        return ticksExisted > 0 && intervalPredic.test(ticksExisted % tick);
    }

    public static long minuteElapsed(World world, long oldTime) {
        return minuteElapsed(worldTicks(world), oldTime);
    }

    public static long minuteElapsed(long nowTime, long oldTime) {
        return (nowTime - oldTime) / 1200;
    }

    public static boolean isMinuteElapsed(World world, long oldTime, int minute) {
        return isMinuteElapsed(worldTicks(world), oldTime, minute);
    }

    public static boolean isMinuteElapsed(long nowTime, long oldTime, int minute) {
        return nowTime - oldTime >= tickFromMinute(minute);
    }

    public static String getTimeString(int cd) {
        int[] timeArray = getTimeArray(cd);
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, timeArray.length).filter(i -> timeArray[i] > 0).findFirst().ifPresent(first -> {
            builder.append(String.format("%02d", timeArray[first]));
            IntStream.range(first + 1, timeArray.length).forEach(unit -> builder.append(":").append(String.format("%02d", timeArray[unit])));
        });
        return builder.toString();
    }

    public static int[] getTimeArray(int cd) {
        int[] timeArray = new int[3];
        timeArray[0] = cd / 72000;
        if (timeArray[0] > 0) {
            cd -= timeArray[0] * 72000;
        }
        timeArray[1] = cd / 1200;
        if (timeArray[1] > 0) {
            cd -= timeArray[1] * 1200;
        }
        timeArray[2] = cd / 20;
        return timeArray;
    }
}
