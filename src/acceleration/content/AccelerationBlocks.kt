package acceleration.content

import mindustry.ctype.*
import mindustry.type.*

import mindustry.world.blocks.defense.*
import mindustry.world.*

import mindustry.content.*

import acceleration.world.blocks.storage.*

class AccelerationBlocks : ContentList {
    override fun load() {
        // Cores

        coreAtom = object : MenderCoreTurret("atom-core") {
            init {
                requirements(Category.effect, ItemStack.with(
                    Items.copper, 24000,
                    Items.lead, 24000,
                    Items.thorium, 12000,
                    Items.silicon, 12000,
                    Items.surgeAlloy, 8000
                ))

                size = 6
                health = 48000
            }
        }

        // Walls

        metaglassWall = object : Wall("metaglass-wall") {
            init {
                requirements(Category.defense, ItemStack.with(Items.metaglass, 5, Items.titanium, 2))

                size = 1
                health = 360
            }
        }

        metaglassWallLarge = object : Wall("metaglass-wall-large") {
            init {
                requirements(Category.defense, ItemStack.with(Items.metaglass, 20, Items.titanium, 8))

                size = 2
                health = 360 * 4
            }
        }
    }

    companion object {
        lateinit var metaglassWall: Block
        lateinit var metaglassWallLarge: Block
        lateinit var coreAtom: Block
    }
}