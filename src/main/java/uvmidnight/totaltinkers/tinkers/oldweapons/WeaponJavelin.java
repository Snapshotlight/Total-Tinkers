package uvmidnight.totaltinkers.tinkers.oldweapons;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.melee.item.Rapier;
import slimeknights.tconstruct.tools.traits.TraitEnderference;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.tinkers.oldweapons.entities.EntityJavelin;

import javax.annotation.Nonnull;
import java.util.List;

//I loved the javelin, it was the reason for me creating this mod
//But lets be honest, it was total garbage
//Meele damage is significantly increased over throwing damage, to act as a sort of hybrid weapon.
//Alternative config option gives it similarity with the cutlass NYI
public class WeaponJavelin extends ProjectileCore {
  private static PartMaterialType rodPMT = new PartMaterialType(TinkerTools.toughToolRod, MaterialTypes.EXTRA, MaterialTypes.PROJECTILE);

  public WeaponJavelin() {
    super(rodPMT, PartMaterialType.arrowHead(TinkerTools.arrowHead), rodPMT);
    addCategory(Category.PROJECTILE);
    setTranslationKey("javelin").setRegistryName("javelin");
  }
  @Override
  public ProjectileNBT buildTagData(List<Material> materials) {
    ProjectileNBT data = new ProjectileNBT();
    HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    data.head(head);
    data.extra(materials.get(0).getStatsOrUnknown(MaterialTypes.EXTRA),
            materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA));
    data.attack += 2;
    return data;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if(ToolHelper.isBroken(itemStackIn)) {
      return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
    }
    playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 8);

    if(!worldIn.isRemote) {
      boolean usedAmmo = useAmmo(itemStackIn, playerIn);
      EntityProjectileBase projectile = getProjectile(itemStackIn, itemStackIn, worldIn, playerIn, 2.1f, 0f, 1f, usedAmmo);
      worldIn.spawnEntity(projectile);
      TotalTinkers.logger.info("entity spawned on server");
    }

    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }


  //TODO: tool consumes stack of stuff instead of durability, which this tool should not be seen to have. Need to override getinformation and some other stuff
  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    return super.onLeftClickEntity(stack, player, entity);
  }

    @Override
  public float damagePotential() {
    return 1.2F;
  }

  @Override
  public EntityProjectileBase getProjectile(@Nonnull ItemStack stack, @Nonnull ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
    inaccuracy *= ProjectileNBT.from(stack).accuracy;
    return new EntityJavelin(world, player, speed, inaccuracy, getProjectileStack(stack, world, player, usedAmmo), launcher);
  }
}