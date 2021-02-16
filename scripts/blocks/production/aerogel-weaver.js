const aerogelWeaver = extend(GenericCrafter, "aerogel-weaver", {
	drawer: DrawWeave(),
	health: 100,
	size: 3,
	solid: true,
	hasPower: true,
	hasItems: true,
	hasLiquid: true,
	itemCapacity: 30,
	liquidCapacity: 10,
	requirements: ItemStack.with(Items.surgeAlloy, 90, Items.phaseFabric, 30, Items.titanium, 120, Items.lead, 150 ,Items.silicon, 180),
	category: Category.crafting,
	outputItem: new ItemStack(Vars.content.getByName(ContentType.item, "acceleration-aerogel"), 1),
	craftTime: 300,
	buildVisibility: BuildVisibility.shown
});

aerogelWeaver.consumes.power(16);
aerogelWeaver.consumes.items(ItemStack.with(Items.sand, 5));
aerogelWeaver.consumes.liquid(Vars.content.getByName(ContentType.liquid, "acceleration-corrofluid"), 0.3)