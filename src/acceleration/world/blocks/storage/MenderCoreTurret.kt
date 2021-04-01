package acceleration.world.blocks.storage

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import arc.graphics.g2d.TextureRegion
import arc.math.Angles
import arc.math.Mathf
import arc.math.geom.Vec2
import arc.util.Time
import arc.util.Tmp
import mindustry.Vars
import mindustry.content.Fx
import mindustry.entities.Lightning
import mindustry.entities.Units
import mindustry.game.Team
import mindustry.gen.Teamc
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.blocks.storage.CoreBlock
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import kotlin.math.abs


open class MenderCoreTurret(name: String) : CoreBlock(name) {

    open var rotateSpeed = 2f
    open var range = (50 * Vars.tilesize).toFloat()
    open var baseColor: Color = Color.valueOf("84f491")
    open var healPercent = 45f
    open var mendReload = 400f
    open var turretReload = 30f
    open var lightningColor: Color = Pal.surge
    open var damage = 20f
    open var turretLightningLength = 50
    open var recoilAmount = 5f
    open var squareSpeed = 0.15f
    open var sizeCap = 24f
    open var lineCap = 5f
    open var lineSpeed = (0.17 / 6).toFloat()

    open var turretRegion: TextureRegion? = null
    open var mendRegion: TextureRegion? = null

    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        super.drawPlace(x, y, rotation, valid)

        Drawf.dashCircle((x * Vars.tilesize).toFloat(), (y * Vars.tilesize).toFloat(), range, baseColor)

        Vars.indexer.eachBlock(Vars.player.team(), (x * Vars.tilesize).toFloat(), (y * Vars.tilesize).toFloat(), range, {true}, { b ->
            Drawf.selected(b, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f)))
        })
    }

    override fun setStats() {
        super.setStats()

        stats.add(Stat.repairTime, (100 / healPercent * mendReload / 60), StatUnit.seconds)
        stats.add(Stat.range, range / Vars.tilesize, StatUnit.blocks)
    }

    override fun load() {
        super.load()

        turretRegion = Core.atlas.find("$name-turret")
        mendRegion = Core.atlas.find("$name-mend")
    }

    override fun icons(): Array<TextureRegion> {
        return arrayOf(region, teamRegion, turretRegion!!)
    }

    inner class MenderCoreTurretBuild : CoreBlock.CoreBuild() {
        private var charge = 0f
        private var squareSize = 0f
        private var lineSize = 0f
        private var cooldown = 0f
        private var turretRotation = 270f
        private var target: Teamc? = null
        private var recoil = 0f

        private fun findTarget(team: Team, x: Float, y: Float, sort: Units.Sortf): Teamc? {
            return Units.bestTarget(team, x, y, range, {u -> !u.dead}, {true}, sort)
        }

        private fun posTarget(target: Teamc?) {
            if (target != null) {
                if (target.team() != team) {
                    turretRotation = Angles.moveToward(turretRotation, angleTo(target.x, target.y), rotateSpeed * Time.delta)
                }
            }
        }

        private fun validateShoot(target: Teamc?): Boolean {
            if (target == null) return false

            return abs(angleTo(target.x, target.y) - turretRotation) <= 5 && cooldown >= turretReload
        }

        private fun shoot(target: Teamc?) {
            if (target != null) {
                cooldown = 0f

                if (target.team() != team) {
                    Lightning.create(team, lightningColor, damage, x, y, turretRotation, turretLightningLength)
                    recoil -= recoilAmount
                }
            }
        }

        private fun mend() {
            charge = 0f

            Vars.indexer.eachBlock(this, range, {b -> b.damaged()}, {b ->
                b.heal(b.maxHealth * (healPercent / 100))
                Fx.healBlockFull.at(b.x, b.y, b.block.size.toFloat(), baseColor)
            })
        }

        private fun updateRecoil() {
            if (recoil < 0) recoil += recoilAmount / turretReload * Time.delta else recoil = 0f
        }

        override fun updateTile() {
            super.updateTile()

            target = findTarget(team, x, y) { u, _, _ -> u.maxHealth }

            updateRecoil()

            charge += Time.delta
            cooldown += Time.delta

            if (charge >= mendReload) mend()

            posTarget((target))

            if (validateShoot(target)) shoot(target)
        }

        override fun drawSelect() {
            super.drawSelect()

            Drawf.dashCircle(x, y, range, baseColor)

            Vars.indexer.eachBlock(Vars.player.team(), x * Vars.tilesize, y * Vars.tilesize, range, {true}, {b ->
                Drawf.selected(b, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f)))
            })
        }

        override fun draw() {
            super.draw()

            Draw.z(Layer.turret)

            val tr = Vec2()
            tr.trns(turretRotation, recoil)
            Draw.rect(turretRegion, x + tr.x, y + tr.y, turretRotation)
            Draw.reset()

            Draw.z(Layer.block)
            Draw.color(baseColor)
            Draw.alpha(Mathf.absin(Time.time, 10f, 1f))
            Draw.rect(mendRegion, x, y)
            Draw.reset()

            val f = 1f - (Time.time / 100f) % 1f

            Draw.z(Layer.turret - 1)
            Draw.color(baseColor)
            Draw.alpha(1f)
            Lines.stroke(f * block.size + 0.15f)
            Lines.square(x, y, (1f + (1f - f) * size * Vars.tilesize / 2f).coerceAtMost(size * Vars.tilesize / 2f))
            Draw.reset()
        }
    }
}