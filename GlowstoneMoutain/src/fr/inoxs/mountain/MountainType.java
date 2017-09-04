package fr.inoxs.mountain;
/*
    @Author : CTRL
 */
public enum MountainType {

    GLOWSTONE,ORE;
    static MountainType cuurentMountainType;
    public static MountainType getCurrentType() {
        return cuurentMountainType;
    }

    public static void setMountainType(MountainType type) {
        MountainType.cuurentMountainType = type;
    }

}
