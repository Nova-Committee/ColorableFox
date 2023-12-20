package committee.nova.colorablefox.mixin;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class MixinAnimal extends AgeableEntity {
    protected MixinAnimal(EntityType<? extends AgeableEntity> t, World l) {
        super(t, l);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void inject$mobInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        if (!((AnimalEntity) (Object) this instanceof FoxEntity)) return;
        final FoxEntity fox = (FoxEntity) (Object) this;
        if (level.isClientSide()) return;
        final ItemStack stack = player.getItemInHand(hand);
        final FoxEntity.Type foxType = fox.getFoxType();
        final Item item = stack.getItem();
        if (item.equals(Items.WHITE_DYE) && foxType.equals(FoxEntity.Type.RED)) {
            fox.setFoxType(FoxEntity.Type.SNOW);
            stack.shrink(1);
            cir.setReturnValue(ActionResultType.SUCCESS);
        } else if (foxType.equals(FoxEntity.Type.SNOW) && (item.equals(Items.RED_DYE) || item.equals(Items.ORANGE_DYE))) {
            fox.setFoxType(FoxEntity.Type.RED);
            stack.shrink(1);
            cir.setReturnValue(ActionResultType.SUCCESS);
        }
    }
}
