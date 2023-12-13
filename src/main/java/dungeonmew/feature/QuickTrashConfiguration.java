package dungeonmew.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public record QuickTrashConfiguration(int[] idsToRemove) {
    public int[] getIDs (){return idsToRemove;}
}
