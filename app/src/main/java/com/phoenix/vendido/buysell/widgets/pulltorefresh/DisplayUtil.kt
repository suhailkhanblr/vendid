/*
 * Copyright (C) RECRUIT LIFESTYLE CO., LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phoenix.vendido.buysell.widgets.pulltorefresh

import android.content.Context

/**
 * @author amyu
 */
class DisplayUtil {

    /**
     * 現在の向きが600dpを超えているかどうか
     *
     * @param context [Context]
     * @return 600dpを超えているかどうか
     */
    companion object {

        fun isOver600dp(context: Context): Boolean {
            val resources = context.resources
            val displayMetrics = resources.displayMetrics
            return displayMetrics.widthPixels / displayMetrics.density >= 600
        }
    }

}
