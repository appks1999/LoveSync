package com.akito.lovesync.data

data class MatchingQuestion(
    val id: Int,
    val text: String,
    val options: List<String>
)

object MatchingShindanContent {
    val matchingQuestions = listOf(
        MatchingQuestion(1, "理想のデート頻度は？", listOf("毎日でも会いたい", "週に1〜2回", "月に数回", "お互いの時間を優先")),
        MatchingQuestion(2, "デート代の支払いは？", listOf("誘った方が払う", "割り勘", "余裕がある方が多めに", "交互に出し合う")),
        MatchingQuestion(3, "どこからが浮気？", listOf("二人で食事", "手を繋ぐ", "隠し事をする", "好きになったら")),
        MatchingQuestion(4, "記念日はどう過ごしたい？", listOf("豪華にお祝い", "いつも通りでいい", "旅行に行きたい", "プレゼント交換は必須")),
        MatchingQuestion(5, "LINEの返信速度は？", listOf("即レス希望", "数時間以内ならOK", "1日1回程度でいい", "返せる時に返す")),
        MatchingQuestion(6, "恋人に秘密はあってもいい？", listOf("隠し事は一切なし", "少しならあってもいい", "プライバシーは別", "聞かれるまで言わない")),
        MatchingQuestion(7, "結婚を意識するタイミングは？", listOf("付き合う前から", "1年以内", "3年以上経ってから", "考えたことがない")),
        MatchingQuestion(8, "喧嘩をした時は？", listOf("その日のうちに解決", "冷静になるまで置く", "相手から謝ってほしい", "とことん話し合う")),
        MatchingQuestion(9, "SNSへの投稿は？", listOf("二人の写真を載せたい", "許可があればOK", "絶対載せたくない", "相手にお任せ")),
        MatchingQuestion(10, "理想の休日の過ごし方は？", listOf("家でまったり", "アクティブに外出", "お互い別々に過ごす", "友達も交えて遊ぶ")),
        MatchingQuestion(11, "相手のスマホは見たい？", listOf("絶対に見たい", "怪しい時だけ見たい", "全く見たくない", "お互いに見せ合うのが理想")),
        MatchingQuestion(12, "子供は欲しい？", listOf("絶対に欲しい", "いつかは欲しい", "どちらでもいい", "欲しくない")),
        MatchingQuestion(13, "家事の分担はどうする？", listOf("きっちり半分", "得意な方がやる", "気づいた方がやる", "基本は相手に任せたい")),
        MatchingQuestion(14, "異性の友達と二人で会うのは？", listOf("全然OK", "事前に言えばOK", "できれば控えてほしい", "絶対にNG")),
        MatchingQuestion(15, "愛情表現は？", listOf("言葉でたくさん言ってほしい", "行動で示してほしい", "適度にあればいい", "恥ずかしいので少なめで")),
        MatchingQuestion(16, "理想の住まいは？", listOf("便利な都会", "落ち着いた郊外", "自然豊かな田舎", "相手に合わせる")),
        MatchingQuestion(17, "お金で重視するのは？", listOf("将来のための貯金", "趣味や自分磨き", "二人の思い出（旅行等）", "日々の生活の質")),
        MatchingQuestion(18, "どちらがリードしたい？", listOf("自分がリードしたい", "相手にリードしてほしい", "二人で相談して決めたい", "状況に合わせて柔軟に")),
        MatchingQuestion(19, "大切にしたい記念日は？", listOf("誕生日と記念日", "誕生日だけでいい", "季節のイベント（クリスマス等）", "何気ない日常が一番")),
        MatchingQuestion(20, "将来の夢や目標は？", listOf("二人で共有して支え合いたい", "お互い個別に追いかけたい", "まずは安定した生活を築きたい", "自然な流れに任せたい"))
    )
}
