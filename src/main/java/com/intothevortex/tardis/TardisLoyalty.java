package com.intothevortex.tardis;

public record TardisLoyalty(int level, Rank rank) {
    public TardisLoyalty {
        level = Math.clamp(level, Rank.REJECT.level, Rank.OWNER.level);
        rank = Rank.fromLevel(level);
    }

    public TardisLoyalty(Rank rank) {
        this(rank.level, rank);
    }

    public boolean isAtLeast(Rank required) {
        return level >= required.level;
    }

    public static TardisLoyalty fromLevel(int level) {
        return new TardisLoyalty(level, Rank.fromLevel(level));
    }

    public enum Rank {
        REJECT(0), NEUTRAL(125), COMPANION(245), PILOT(450), OWNER(500);

        public final int level;

        Rank(int level) {
            this.level = level;
        }

        public static Rank fromLevel(int level) {
            int normalized = Math.clamp(level, REJECT.level, OWNER.level);
            Rank result = REJECT;
            for (Rank value : values()) if (value.level <= normalized) result = value;
            return result;
        }
    }
}
