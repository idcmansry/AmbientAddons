import com.ambientaddons.commands.AmbientCommand
import com.ambientaddons.config.Config
import com.ambientaddons.config.PersistentData
import com.ambientaddons.features.display.WitherShieldOverlay
import com.ambientaddons.features.dungeon.*
import com.ambientaddons.features.dungeon.terminals.MelodyHelper
import com.ambientaddons.features.misc.BonzoMask
import com.ambientaddons.features.keybinds.PerspectiveKeybind
import com.ambientaddons.features.keybinds.SendLastMessageKeybind
import com.ambientaddons.features.misc.CancelInteractions
import com.ambientaddons.features.misc.KuudraReady
import com.ambientaddons.utils.SkyBlock
import com.ambientaddons.utils.dungeon.DungeonPlayers
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModMetadata
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import java.io.File

@Mod(
    modid = "ambientaddons",
    name = "AmbientAddons",
    version = "0.1",
    useMetadata = true,
    clientSideOnly = true
)
class AmbientAddons {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        metadata = event.modMetadata
        val directory = File(event.modConfigurationDirectory, "ambientaddons-forge")
        directory.mkdirs()
        configDirectory = directory
        persistentData = PersistentData.load()
        config = Config.apply { this.initialize() }
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(AmbientCommand())
        listOf(
            this,
            SkyBlock,
            AutoBuyChest,
            CloseChest,
            CancelInteractions,
            DungeonReady,
            ShortbowClicker,
            BonzoMask,
            PerspectiveKeybind,
            SendLastMessageKeybind,
            DungeonPlayers,
            MelodyHelper,
            WitherShieldOverlay,
            KuudraReady,
            DungeonHighlights
        ).forEach(MinecraftForge.EVENT_BUS::register)
        keyBinds.values.forEach(ClientRegistry::registerKeyBinding)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || currentGui == null) return
        mc.displayGuiScreen(currentGui)
        currentGui = null
    }

    companion object {
        fun isInitialized(): Boolean {
            return ::config.isInitialized
        }

        val mc: Minecraft = Minecraft.getMinecraft()

        val keyBinds = mapOf(
            "thirdPersonKey" to KeyBinding("Toggle third-person perspective", Keyboard.KEY_NONE, "AmbientAddons"),
            "secondPersonKey" to KeyBinding("Toggle second-person perspective", Keyboard.KEY_NONE, "AmbientAddons"),
            "spamKey" to KeyBinding("Send last message in party chat", Keyboard.KEY_NONE, "AmbientAddons"),
        )

        var currentGui: GuiScreen? = null

        lateinit var configDirectory: File
        lateinit var config: Config
        lateinit var persistentData: PersistentData

        lateinit var metadata: ModMetadata
    }



}