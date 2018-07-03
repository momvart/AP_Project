package graphics.helpers;

import graphics.layers.Layer;

public class WizardBulletHelper extends BulletHelper
{
    public WizardBulletHelper(DefensiveTowerGraphicHelper towerGraphicHelper, Layer layer)
    {
        super(towerGraphicHelper, layer);
    }

    @Override
    public void onMoveFinish()
    {
        hitTarget = true;
        drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
        inProgress = false;
        towerGraphicHelper.onBulletHit(DefenseKind.AREA_SPLASH);
        start = null;
        end = null;
        targetSoldier = null;
        cos = -1;
        sin = -1;
    }
}
