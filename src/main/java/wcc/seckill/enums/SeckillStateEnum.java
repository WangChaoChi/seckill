package wcc.seckill.enums;

/**
 * @author wangcc
 * @decription: 使用枚举表述常量数据字段
 * @date 2018/2/7 16:46
 */
public enum SeckillStateEnum {
    SUCCESS(1,"秒杀结束"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATE_REWRITE(-3,"数据篡改");

    private int state;

    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 根据状态获得枚举
     *
     * @param index
     * @return
     */
    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
