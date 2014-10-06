package de.woitek.freetrader1902scorekeeper.types;

import java.util.Observable;

public class GameFight extends Observable implements GameEvent {
    private final GameData gameData;
    private Enemy mEnemy = Enemy.NOBODY;
    private Fight mFight = Fight.NOFIGHT;
    private int mEnemyMight = 0;
    private int mEnemyModifier = 0;
    private int mPlayerModifier = 0;

    public GameFight(GameData gameData) {
        this.gameData = gameData;
    }

	public GameFight(GameData gameData, Enemy enemy, int enemyMight) {
		this.gameData = gameData;
		this.mEnemy = enemy;
		this.mEnemyMight = (mEnemy == Enemy.BEAR)? 2: enemyMight;
	}

    protected void notifyIfFightStateChanged(Command cmd) {
        if (cmd.execute()) {
            setChanged();
            notifyObservers();
        }
    }

    public void setEnemyMight(final int might) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public boolean execute() {
                boolean changed = (mEnemyMight != might);
                mEnemyMight = might;
                return changed;
            }
        });
    }

    public void setEnemyModifier(final int modifier) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public boolean execute() {
                boolean changed = (mEnemyModifier != modifier);
                mEnemyModifier = modifier;
                return changed;
            }
        });
    }

    public void setPlayerModifier(final int modifier) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public boolean execute() {
                boolean changed = (mPlayerModifier != modifier);
                mPlayerModifier = modifier;
                return changed;
            }
        });
    }

    public boolean canFight() {
        return (mEnemy != Enemy.NOBODY) && (mFight != Fight.NOFIGHT)
                && ((mEnemy == Enemy.BEAR) || (mEnemyMight > 0))
                && (mEnemyModifier > 0) && (mPlayerModifier > 0);
    }

    public int getPlayerFightValue() {
        int value = 0;
        if (canFight()) {
            value += mPlayerModifier;
            value += (mFight == Fight.OFFENSE)
                    ? gameData.getEquipment(GameData.SHOTGUNS)
                    : gameData.getEquipment(GameData.ARMOR);
        }
        return value;
    }

    public int getEnemyFightValue() {
        int value = 0;
        if (canFight()) {
            if (mEnemy == Enemy.BEAR) {
                value += 2;
                value += 2 * mEnemyModifier;
            } else {
                value += mEnemyMight;
                value += mEnemyModifier;
            }
            value += gameData.getMonth() - 1;
        }
        return value;
    }

    public boolean hasPlayerWon() {
        boolean won = false;
        if (canFight()) {
            if (mFight == Fight.OFFENSE) {
                won = getPlayerFightValue() > getEnemyFightValue();
            } else {
                won = getEnemyFightValue() <= getPlayerFightValue();
            }
        }
        return won;
    }

    public Enemy getEnemy() {
        return mEnemy;
    }

    public void setEnemy(final Enemy enemy) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public boolean execute() {
                boolean changed = (mEnemy != enemy);
                mEnemy = enemy;
                return changed;
            }
        });
    }

    public int getEnemyBonusValue() {
        int bonus = 0;
        if (mEnemy == Enemy.HIGHWAYMAN) {
            switch (mEnemyMight) {
                case 1:
                    bonus = 1;
                    break;
                case 2:
                    bonus = 3;
                    break;
                case 3:
                    bonus = 4;
                    break;
                default:
                    break;
            }
        }
        return bonus;
    }

    public Fight getFight() {
        return mFight;
    }

    public void setFight(final Fight fight) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public boolean execute() {
                boolean changed = (mFight != fight);
                mFight = fight;
                return changed;
            }
        });
    }

	@Override
	public EventType getEventType() {
		return EventType.FIGHT;
	}

	public enum Enemy {
        BEAR,
        HIGHWAYMAN,
	    CONSTABLE, NOBODY
    }

    public enum Fight {
        OFFENSE,
        DEFENSE,
        NOFIGHT
    }

    public interface Command {
        boolean execute();
    }
}
