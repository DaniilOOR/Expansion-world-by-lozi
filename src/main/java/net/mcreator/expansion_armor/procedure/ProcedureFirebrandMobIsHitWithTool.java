package net.mcreator.expansion_armor.procedure;

import net.minecraft.entity.Entity;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureFirebrandMobIsHitWithTool extends ElementsExpansionarmor.ModElement {
	public ProcedureFirebrandMobIsHitWithTool(ElementsExpansionarmor instance) {
		super(instance, 102);
	}

	public static void executeProcedure(java.util.HashMap<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			System.err.println("Failed to load dependency entity for procedure FirebrandMobIsHitWithTool!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		entity.setFire((int) 6);
	}
}
