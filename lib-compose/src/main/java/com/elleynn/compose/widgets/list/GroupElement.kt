package com.elleynn.compose.widgets.list

class GroupElement(
    var isExpanded: Boolean = true,
    override val key: Any,
    val children: MutableList<Element> = mutableListOf()
) : Element {

}