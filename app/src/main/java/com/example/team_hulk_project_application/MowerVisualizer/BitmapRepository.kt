import com.example.team_hulk_project_application.MowerVisualizer.ImageLayer
import com.example.team_hulk_project_application.MowerVisualizer.ImageLayerKeyword

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.team_hulk_project_application.R

val bitmapRepository = BitmapRepository()

class BitmapRepository {
    private val mower1 = mutableListOf<ImageLayer>()

    fun changeVisibilityByKeyword(key: ImageLayerKeyword, visible: Boolean) {
        mower1.find {
            it.keyWord == key
        }?.run {
            isVisible = visible
        }
    }

    fun getMutableList() : MutableList<ImageLayer>? {
        return mower1;
    }

    fun setLayersHidden() {
        for (x in mower1) {
            changeVisibilityByKeyword(x.keyWord!!, false)
        }
    }

    fun init(res: Resources, finished: (Boolean) -> Unit) {
        /* Initialize Bitmap-Repository here */

        // BASE
        var bitmap: Bitmap = BitmapFactory.decodeResource(res, R.drawable.mower_base_temp_min)
        var layer = ImageLayer(bitmap, true, ImageLayerKeyword.MowerBase)
        mower1.add(layer)

        // Collision Indicator 1
        bitmap = BitmapFactory.decodeResource(res, R.drawable.mower_collision1_temp_min)
        layer = ImageLayer(bitmap, true, ImageLayerKeyword.MowerCollision1)
        mower1.add(layer)

        // Collision Indicator 2
        bitmap = BitmapFactory.decodeResource(res, R.drawable.mower_collision2_temp_min)
        layer = ImageLayer(bitmap, true, ImageLayerKeyword.MowerCollision2)
        mower1.add(layer)

        // Collision Indicator 3
        bitmap = BitmapFactory.decodeResource(res, R.drawable.mower_collision3_temp_min)
        layer = ImageLayer(bitmap, true, ImageLayerKeyword.MowerCollision3)
        mower1.add(layer)

        /* - - - - - - - - - - - - - - - - - */

        finished(true)
    }
}