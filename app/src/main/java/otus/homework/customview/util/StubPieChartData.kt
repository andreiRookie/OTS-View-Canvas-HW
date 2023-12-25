package otus.homework.customview.util

import otus.homework.customview.presentation.PieChartModel
import otus.homework.customview.presentation.SectorModel

private val sectors = listOf(
    SectorModel(name="Продукты", sweepAngle=33.0f, color=-3030052),
    SectorModel(name="Здоровье", sweepAngle=78.0f, color=-15143197),
    SectorModel(name="Кафе и рестораны", sweepAngle=35.0f, color=-4305503),
    SectorModel(name="Алкоголь", sweepAngle=12.0f, color=-14023128),
    SectorModel(name="Доставка еды", sweepAngle=14.0f, color=-8851723),
    SectorModel(name="Транспорт", sweepAngle=7.0f, color=-13062620),
    SectorModel(name="Спорт", sweepAngle=22.0f, color=-16745766),
    SectorModel(name="Развлечения", sweepAngle=29.0f, color=-4700902),
    SectorModel(name="Одежда", sweepAngle=29.0f, color=-12192542),
    SectorModel(name="Образование", sweepAngle=101.0f, color=-644795)
)

internal val stubPieChartModel = PieChartModel(sectors)

