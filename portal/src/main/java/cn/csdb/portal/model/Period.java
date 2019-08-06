package cn.csdb.portal.model;

/**
 * @Author jinbao
 * @Date 2019/5/16 15:09
 * @Description 频率换算毫秒值
 **/
public enum Period {
    TERMINATION(0L),
    HALF_OF_THE_DAY(1000L * 60 * 60 * 12),
    DAY(1000L * 60 * 60 * 24),
    WEEK(1000L * 60 * 60 * 24 * 7),
    MONTH(1000L * 60 * 60 * 24 * 30),
    YEAR(1000L * 60 * 60 * 24 * 365);

    private final long dataTime;

    private Period(long dataTime) {
        this.dataTime = dataTime;
    }

    public long getDataTime() {
        return this.dataTime;
    }

    /**
     * 根据毫秒值反向获得key
     *
     * @param dataTime
     * @return
     */
    public static Period selectPeriodByDataTime(Long dataTime) {
        for (Period period : Period.values()) {
            boolean equalTime = period.getDataTime() == dataTime;
            if (equalTime) {
                return period;
            }
        }
        return null;
    }

}
