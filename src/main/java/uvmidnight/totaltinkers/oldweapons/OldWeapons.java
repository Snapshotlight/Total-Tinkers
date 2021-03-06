package uvmidnight.totaltinkers.oldweapons;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import uvmidnight.totaltinkers.IModule;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.TotalTinkersRegister;

public class OldWeapons extends IModule {
    final static String CategoryName = "Old Tools";

    public static Property battleaxeEnabled;
    public static Property cutlassEnabled;
    public static Property javelinEnabled;
    public static Property daggerEnabled;
    public static Property isReplacingCrossbow;

    public static Property cutlassSpeedDuration;
    public static Property cutlassSpeedStrength;

    public static Property javelinLegacyMode;
//    public static Property disableBattleaxeScreenOverlay;
//    public static Property battleaxeOverlayNew;

    public static Property battleaxeIncreasedDamage;

    public static Property crossbowOldCrosshair;
    public static Property autoCrossbowReload;
    public static Property autoCrossbowDualWield;

    public static Property fullGuardCraftable;
    public static Property fullGuardFromVillages;
    public static ToolPart fullGuard;

    public static WeaponBattleAxe battleaxe;
    public static WeaponCutlass cutlass;
//    public static PotionBerserker potionBerserker;
    public static WeaponJavelin javelin;
    public static WeaponDagger dagger;

    public OldWeapons(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryName, "Configuration for the old 1.7 weapons, reborn anew");

        battleaxeEnabled = cfg.get(CategoryName, "Battle Axe Enabled?", true, "If the swirling whirlwind of death will slay");
        cutlassEnabled = cfg.get(CategoryName, "Cutlass Enabled?", true, "Here be the sword of the seas.");
        javelinEnabled = cfg.get(CategoryName, "Javelin Enabled", true, "If the warframe's worst throwing weapon is enabled");
        daggerEnabled = cfg.get(CategoryName, "Dagger Enabled?", true, "Should the rouge's weapon, the dagger, be enabled");

        cutlassSpeedDuration = cfg.get(CategoryName, "Cutlass Speed Effect Duration", 30, "How many ticks of speed to give", 0, Integer.MAX_VALUE);
        cutlassSpeedStrength = cfg.get(CategoryName, "Cutlass Speed Effect Strength", 2, "What speed amplitude to give. 3 is default", -1, Short.MAX_VALUE);

        fullGuardFromVillages = cfg.get(CategoryName, "Full Guard From Village", true, "Should the full guard pattern come from villages. Disable to make it craftable in the stencil table.");
        fullGuardCraftable = cfg.get(CategoryName, "Full Guard Craftable", true, "If the full guard should be obtainable via one of the two normal ways. If you wish to add your own recipe, set this to false.");


        battleaxeIncreasedDamage = cfg.get(CategoryName, "Battleaxe Increased damage Taken", 2.0, "Multiplier for damage taken while using battleaxe.", Short.MIN_VALUE, Short.MAX_VALUE);

        javelinLegacyMode = cfg.get(CategoryName, "Javelin Legacy Mode", false, "Enable legacy mode for the javelin. Rather than charging the javelin, javelins are instantly thrown but have a moderate cooldown between throws.");

        isReplacingCrossbow = cfg.get(CategoryName, "Replace Tinker's Crossbow", false, "Should the crossbow be replaced by a custom version. This is REQUIRED for any of the crossbow tweaks");

        crossbowOldCrosshair = cfg.get(CategoryName, "Crossbow Crosshair", true, "If the old crossbow cursor should be used");
        autoCrossbowReload = cfg.get(CategoryName, "Crossbow Automatic Reload After Shooting", false, "If enabled, the crossbow will automatically reload after being shot.");
        autoCrossbowDualWield = cfg.get(CategoryName, "Crossbow Apply automatic behavior to dual wield", true, "If enabled, the crossbow will still automatically reload while in offhand");
//    autoCrossbowSlowdown = cfg.getBoolean("autoCrossbowSlowdown", CATEGORY_CROSSBOW, true, "If the automatic reload function is enabled, the crossbow will slow the player while reloading");
    }

    @Override
    public void initItems(RegistryEvent.Register<Item> event) {
        if (OldWeapons.cutlassEnabled.getBoolean()) {
            fullGuard = new ToolPart(Material.VALUE_Ingot * 3);
            fullGuard.setTranslationKey("fullGuard").setRegistryName("fullGuard");
            event.getRegistry().register(fullGuard);
            TinkerRegistry.registerToolPart(fullGuard);
            TotalTinkers.proxy.registerToolPartModel(fullGuard);
            if (!OldWeapons.fullGuardFromVillages.getBoolean() && fullGuardCraftable.getBoolean()) {
                TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), fullGuard));
            }
        }
        if (OldWeapons.battleaxeEnabled.getBoolean()) {
            OldWeapons.battleaxe = new WeaponBattleAxe();
            TotalTinkersRegister.initForgeTool(OldWeapons.battleaxe, event);
        }
        if (OldWeapons.cutlassEnabled.getBoolean()) {
            OldWeapons.cutlass = new WeaponCutlass();
            TotalTinkersRegister.initForgeTool(OldWeapons.cutlass, event);
        }
        if (OldWeapons.javelinEnabled.getBoolean()) {
            OldWeapons.javelin = new WeaponJavelin();
            event.getRegistry().register(OldWeapons.javelin);
            TinkerRegistry.registerToolStationCrafting(OldWeapons.javelin);
            TinkerRegistry.registerToolForgeCrafting(OldWeapons.javelin);
            TotalTinkers.proxy.registerToolModel(OldWeapons.javelin);
        }
        if (OldWeapons.daggerEnabled.getBoolean()) {
            OldWeapons.dagger = new WeaponDagger();
            event.getRegistry().register(OldWeapons.dagger);
            TinkerRegistry.registerToolForgeCrafting(OldWeapons.dagger);
            TinkerRegistry.registerToolStationCrafting(OldWeapons.dagger);
            TotalTinkers.proxy.registerToolModel(OldWeapons.dagger);
        }
        if (OldWeapons.isReplacingCrossbow.getBoolean()) {
            TinkerRangedWeapons.crossBow = new WeaponCrossbowOveride();
            TotalTinkersRegister.initForgeTool(TinkerRangedWeapons.crossBow, event);
        }
    }
}
