package committee.nova.colorablefox.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class MixinAnimal extends AgeableMob {
    protected MixinAnimal(EntityType<? extends AgeableMob> t, Level l) {
        super(t, l);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void inject$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!((Animal) (Object) this instanceof Fox fox)) return;
        if (level.isClientSide()) return;
        final ItemStack stack = player.getItemInHand(hand);
        final Fox.Type foxType = fox.getFoxType();
        if (stack.is(Items.WHITE_DYE) && foxType.equals(Fox.Type.RED)) {
            fox.setFoxType(Fox.Type.SNOW);
            stack.shrink(1);
            level.playSound(null, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (foxType.equals(Fox.Type.SNOW) && (stack.is(Items.RED_DYE) || stack.is(Items.ORANGE_DYE))) {
            fox.setFoxType(Fox.Type.RED);
            stack.shrink(1);
            level.playSound(null, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
