const ModName = "acceleration-";

function getTextureName(blockName, name) {
	let fullName = ModName + blockName;
	return fullName + name;
};

function getRegion(blockName, name) {
	if (name == null) {
		return Core.atlas.find(ModName + blockName.substr(0, blockName.length-1))
	}
	return Core.atlas.find(ModName + blockName + name)
};

const cblock = name => Vars.content.getByName(ContentType.block, ModName + name);
const cliquid = name => Vars.content.getByName(ContentType.liquid, ModName + name);
const cstatus = name => Vars.content.getByName(ContentType.status, ModName + name);
const citem = name => Vars.content.getByName(ContentType.item, ModName + name);
const percent = (partial, total) => partial / total;
const hexColor = hexValue => Color.valueOf(hexValue);

module.exports = {
	getTextureName: getTextureName,
	getRegion: getRegion,
	cliquid: cliquid,
	citem: citem,
	cblock: cblock,
	cstatus: cstatus,
	percent: percent,
	hexColor: hexColor,
	ModName: ModName
};