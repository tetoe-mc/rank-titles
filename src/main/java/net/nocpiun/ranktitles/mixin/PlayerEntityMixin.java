package net.nocpiun.ranktitles.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.nocpiun.ranktitles.utils.Message;
import net.nocpiun.ranktitles.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "getName", cancellable = true)
    private void getName(CallbackInfoReturnable<Text> info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        info.setReturnValue(Message.colorize(Utils.getPlayerTitlesPrefix(player.getUuidAsString()) + info.getReturnValue().getString()));
    }
}
