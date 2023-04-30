package me.shershnyaga.squests.npc;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;

@TraitName("snps")
public class SNpcTrait extends Trait {
    @Persist
    public String name = "name";

    public SNpcTrait() {
        super("snpc");
    }
}
