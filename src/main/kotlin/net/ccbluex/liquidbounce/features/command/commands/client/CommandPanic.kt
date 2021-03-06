/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2016 - 2021 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.features.command.commands.client

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.command.CommandException
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import net.ccbluex.liquidbounce.features.command.builder.ParameterBuilder
import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.utils.chat

object CommandPanic {

    fun createCommand(): Command {
        return CommandBuilder
            .begin("panic")
            .description("Turns off all modules")
            .parameter(
                ParameterBuilder
                    .begin<String>("category")
                    .description("Specific category of modules")
                    .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                    .optional()
                    .build()
            )
            .handler { args ->
                var modules = ModuleManager.filter { it.enabled }
                val msg: String

                val type = if (args.isNotEmpty()) {
                    args[0] as String
                }else{
                    "nonrender"
                }

                when (type) {
                    "all" -> msg = "all"
                    "nonrender" -> {
                        modules = modules.filter { it.category != Category.RENDER }
                        msg = "all non-render"
                    }
                    else -> {
                        val category = Category.values().find { it.readableName.equals(type, true) } ?:
                            throw CommandException("Category '$type' not found.")
                        modules = modules.filter { it.category == category }
                        msg = "all ${category.readableName}"
                    }
                }

                modules.forEach { it.enabled = false }
                chat("Disabled $msg modules.")
            }
            .build()
    }

}
