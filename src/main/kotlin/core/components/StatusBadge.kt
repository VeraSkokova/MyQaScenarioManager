package core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.DefectStatus
import domain.model.ResultStatus
import domain.model.RunStatus
import domain.model.ScenarioPriority
import domain.model.ScenarioType

private val Green = Color(0xFF2E7D32)
private val GreenBg = Color(0xFFE8F5E9)
private val Red = Color(0xFFC62828)
private val RedBg = Color(0xFFFFEBEE)
private val Orange = Color(0xFFE65100)
private val OrangeBg = Color(0xFFFFF3E0)
private val Blue = Color(0xFF1565C0)
private val BlueBg = Color(0xFFE3F2FD)
private val Gray = Color(0xFF616161)
private val GrayBg = Color(0xFFF5F5F5)
private val Teal = Color(0xFF00897B)
private val TealBg = Color(0xFFE0F2F1)
private val Purple = Color(0xFF5E35B1)
private val PurpleBg = Color(0xFFEDE7F6)

data class BadgeColors(
    val text: Color,
    val background: Color,
)

fun ResultStatus.badgeColors(): BadgeColors = when (this) {
    ResultStatus.PASSED -> BadgeColors(Green, GreenBg)
    ResultStatus.FAILED -> BadgeColors(Red, RedBg)
    ResultStatus.BLOCKED -> BadgeColors(Orange, OrangeBg)
    ResultStatus.NOT_RUN -> BadgeColors(Gray, GrayBg)
}

fun RunStatus.badgeColors(): BadgeColors = when (this) {
    RunStatus.COMPLETED -> BadgeColors(Green, GreenBg)
    RunStatus.IN_PROGRESS -> BadgeColors(Blue, BlueBg)
    RunStatus.DRAFT -> BadgeColors(Gray, GrayBg)
}

fun ScenarioType.badgeColors(): BadgeColors = when (this) {
    ScenarioType.SMOKE -> BadgeColors(Teal, TealBg)
    ScenarioType.FUNCTIONAL -> BadgeColors(Purple, PurpleBg)
}

fun DefectStatus.badgeColors(): BadgeColors = when (this) {
    DefectStatus.OPEN -> BadgeColors(Red, RedBg)
    DefectStatus.IN_PROGRESS -> BadgeColors(Blue, BlueBg)
    DefectStatus.FIXED -> BadgeColors(Green, GreenBg)
    DefectStatus.CLOSED -> BadgeColors(Gray, GrayBg)
    DefectStatus.WONT_FIX -> BadgeColors(Orange, OrangeBg)
}

fun ScenarioPriority.badgeColors(): BadgeColors = when (this) {
    ScenarioPriority.CRITICAL -> BadgeColors(Red, RedBg)
    ScenarioPriority.HIGH -> BadgeColors(Orange, OrangeBg)
    ScenarioPriority.MEDIUM -> BadgeColors(Blue, BlueBg)
    ScenarioPriority.LOW -> BadgeColors(Gray, GrayBg)
}

@Composable
fun StatusBadge(
    text: String,
    colors: BadgeColors,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colors.background)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            color = colors.text,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
