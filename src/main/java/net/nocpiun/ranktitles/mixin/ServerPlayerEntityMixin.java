package net.nocpiun.ranktitles.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.nocpiun.ranktitles.utils.Message;
import net.nocpiun.ranktitles.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "getPlayerListName", cancellable = true)
    private void getName(CallbackInfoReturnable<Text> info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        info.setReturnValue(player.getName());
    }
}
