package de.woitek.freetrader1902scorekeeper.types;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class GameFight {
    public final static String FIGHTEVENT = "GameFightEvent";

    private final GameData gameData;
    private Enemy mEnemy = Enemy.NOBODY;
    private Fight mFight = Fight.NOFIGHT;
    private int mEnemyMight = 0;
    private int mEnemyModifier = 0;
    private int mPlayerModifier = 0;

    private ArrayList<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

    public GameFight(GameData gameData) {
        this.gameData = gameData;
    }

    protected void notifyIfFightStateChanged(Command cmd) {
        boolean oldFightable = canFight();
        cmd.execute();
        boolean newFightable = canFight();
        notifyListeners(this, FIGHTEVENT, oldFightable, newFightable);
    }

    public void setEnemyMight(final int might) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public void execute() {
                mEnemyMight = might;
            }
        });
    }

    public void setEnemyModifier(final int modifier) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public void execute() {
                mEnemyModifier = modifier;
            }
        });
    }

    public void setPlayerModifier(final int modifier) {
        notifyIfFightStateChanged(new Command() {
            @Override
            public void execute() {
                mPlayerModifier = modifier;
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

    private void notifyListeners(Object object, String property, boolean oldValue, boolean newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(object, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    public Enemy getEnemy() {
        return mEnemy;
    }

    public void setEnemy(Enemy enemy) {
        mEnemy = enemy;
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
            public void execute() {
                mFight = fight;
            }
        });
    }

    public enum Enemy {
        BEAR,
        HIGHWAYMAN,
        NOBODY
    }

    public enum Fight {
        OFFENSE,
        DEFENSE,
        NOFIGHT
    }

    public interface Command {
        void execute();
    }
}
