package graphics.helpers;

import models.soldiers.Soldier;
import utils.PointF;

public interface IOnBulletTriggerListener
{
    void onBulletTrigger(PointF targetedPoint, Soldier soldier);
}
