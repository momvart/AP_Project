package network;

import models.attack.AttackMap;

import java.util.UUID;

public interface IOnAttackMapReturnedListener
{
    void onAttackMapReturned(UUID from, AttackMap map);
}
