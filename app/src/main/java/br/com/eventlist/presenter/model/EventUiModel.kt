package br.com.eventlist.presenter.model

import br.com.eventlist.domain.model.Event

class EventUiModel(
    val id: String = "",
    val description: String = "",
    val image: String = "",
    val price: String = "",
    val title: String = ""
)

fun Event.toUiModel() = EventUiModel(
    id = this.id,
    description = this.description,
    image = this.image,
    price = this.price.toReal(),
    title = this.title
)

fun List<Event>.toUiModel() : List<EventUiModel> {
    val list = mutableListOf<EventUiModel>()
    this.forEach { event ->
        list.add(event.toUiModel())
    }
    return list
}

fun Double.toReal() : String {
    return try {
        this.toString().split(".").let { result ->
            val cent = if (result[1].length > 1) result[1] else "${result[1]}0"
            "${result[0]},$cent"
        }
    } catch (e: Exception) {
        this.toString()
    }
}