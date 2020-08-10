package com.bugu.walle.log

import com.bugu.walle.R

enum class LogLevelEnum(val level: Int, val text: String, val color: Int) {
    VERBOSE(0, "V", R.color.log_level_verbose),
    DEBUG(1, "D", R.color.log_level_debug),
    INFO(2, "I", R.color.log_level_info),
    WARN(3, "W", R.color.log_level_warn),
    ERROR(4, "E", R.color.log_level_error),
    ASSERT(5, "A", R.color.log_level_asset)
}