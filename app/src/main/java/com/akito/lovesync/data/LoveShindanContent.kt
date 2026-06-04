package com.akito.lovesync.data

import com.akito.lovesync.ShindanData
import com.akito.lovesync.ShindanOption
import com.akito.lovesync.ShindanQuestion
import com.akito.lovesync.ShindanResult

object LoveShindanContent {
    val soloLoveShindan = ShindanData(
        title = "一人用・恋愛カルテ",
        questions = listOf(
            ShindanQuestion(1, "デートの待ち合わせ、相手が15分遅れてきたら？", listOf(
                ShindanOption("「全然気にしてないよ」と笑顔で迎える", mapOf("HEALING" to 2, "DEVOTION" to 1)),
                ShindanOption("「次からは気をつけてね」と軽く釘を刺す", mapOf("LEADER" to 2, "STRATEGY" to 1)),
                ShindanOption("自分もゆっくり準備できて良かったと前向きに捉える", mapOf("FREE" to 2, "COOL" to 1)),
                ShindanOption("寂しかったよ、と少し甘えてみせる", mapOf("NEEDY" to 2, "PURE" to 1))
            )),
            ShindanQuestion(2, "好きな人へのLINEの頻度は？", listOf(
                ShindanOption("用事がなくても毎日何度もしたい", mapOf("NEEDY" to 2, "PASSION" to 1)),
                ShindanOption("相手のペースに合わせる", mapOf("DEVOTION" to 2, "HEALING" to 1)),
                ShindanOption("必要な連絡事項がある時だけでいい", mapOf("COOL" to 2, "MATURE" to 1)),
                ShindanOption("駆け引きを楽しんで、あえて返信を遅らせることもある", mapOf("STRATEGY" to 2, "LEADER" to 1))
            )),
            ShindanQuestion(3, "告白するならどんなシチュエーション？", listOf(
                ShindanOption("夜景の見えるロマンチックな場所で", mapOf("PASSION" to 2, "PURE" to 1)),
                ShindanOption("いつもの帰り道、さりげなく", mapOf("COOL" to 1, "FREE" to 2)),
                ShindanOption("確実にOKがもらえると確信してから", mapOf("STRATEGY" to 2, "MATURE" to 1)),
                ShindanOption("相手から言ってくれるように仕向ける", mapOf("LEADER" to 1, "STRATEGY" to 2))
            )),
            ShindanQuestion(4, "恋人が他の異性と楽しそうに話していたら？", listOf(
                ShindanOption("激しく嫉妬してしまう", mapOf("PASSION" to 1, "NEEDY" to 2)),
                ShindanOption("信頼しているので気にならない", mapOf("MATURE" to 2, "COOL" to 1)),
                ShindanOption("後で「誰と話してたの？」と可愛く聞く", mapOf("PURE" to 2, "HEALING" to 1)),
                ShindanOption("自分も他の人と楽しそうにして見せつける", mapOf("STRATEGY" to 2, "LEADER" to 1))
            )),
            ShindanQuestion(5, "休日の理想の過ごし方は？", listOf(
                ShindanOption("一日中家で二人きりでまったり", mapOf("HEALING" to 2, "NEEDY" to 1)),
                ShindanOption("話題のスポットへアクティブにお出かけ", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("お互い別々のことをしていても同じ空間にいたい", mapOf("COOL" to 2, "MATURE" to 1)),
                ShindanOption("恋人の行きたい場所に全力で付き合う", mapOf("DEVOTION" to 2, "PURE" to 1))
            )),
            ShindanQuestion(6, "プレゼントを選ぶ時の基準は？", listOf(
                ShindanOption("相手が今一番欲しがっているもの", mapOf("DEVOTION" to 2, "STRATEGY" to 1)),
                ShindanOption("自分のセンスを信じて選ぶ", mapOf("LEADER" to 2, "PASSION" to 1)),
                ShindanOption("ずっと身につけてもらえるペアのもの", mapOf("NEEDY" to 2, "PURE" to 1)),
                ShindanOption("実用的で長く使えるもの", mapOf("MATURE" to 2, "COOL" to 1))
            )),
            ShindanQuestion(7, "ケンカをした時のあなたの対応は？", listOf(
                ShindanOption("自分からすぐに謝って仲直りする", mapOf("HEALING" to 2, "DEVOTION" to 1)),
                ShindanOption("お互いに冷静になるまで時間を置く", mapOf("MATURE" to 2, "COOL" to 1)),
                ShindanOption("自分の意見をはっきり伝えて納得いくまで話し合う", mapOf("LEADER" to 2, "PASSION" to 1)),
                ShindanOption("悲しい気持ちを伝えて相手の反応を待つ", mapOf("PURE" to 2, "NEEDY" to 1))
            )),
            ShindanQuestion(8, "恋人に求める一番の要素は？", listOf(
                ShindanOption("一緒にいてドキドキさせてくれる刺激", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("何でも包み込んでくれる包容力", mapOf("NEEDY" to 2, "HEALING" to 1)),
                ShindanOption("高め合える価値観や知性", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("裏切らない誠実さと一途さ", mapOf("PURE" to 2, "DEVOTION" to 1))
            )),
            ShindanQuestion(9, "二人の将来について考える？", listOf(
                ShindanOption("付き合った瞬間から結婚まで想像する", mapOf("PURE" to 1, "NEEDY" to 2)),
                ShindanOption("今の楽しさが一番大事", mapOf("FREE" to 2, "PASSION" to 1)),
                ShindanOption("計画的に二人の目標を立てたい", mapOf("STRATEGY" to 2, "LEADER" to 1)),
                ShindanOption("自然な流れに任せるのが一番", mapOf("COOL" to 1, "MATURE" to 2))
            )),
            ShindanQuestion(10, "あなたの「浮気」の定義は？", listOf(
                ShindanOption("二人きりで会ったらアウト", mapOf("PASSION" to 1, "PURE" to 2)),
                ShindanOption("手をつないだりスキンシップをしたらアウト", mapOf("DEVOTION" to 2, "NEEDY" to 1)),
                ShindanOption("気持ちが相手に移ったらアウト", mapOf("HEALING" to 1, "MATURE" to 2)),
                ShindanOption("隠し事をされたらそれが何であれアウト", mapOf("LEADER" to 2, "COOL" to 1))
            )),
            ShindanQuestion(11, "恋人と映画を観るなら？", listOf(
                ShindanOption("泣ける純愛ストーリー", mapOf("PURE" to 2, "HEALING" to 1)),
                ShindanOption("ハラハラするアクションやホラー", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("深く考えさせられるヒューマンドラマ", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("相手が観たいと言ったもの", mapOf("DEVOTION" to 2, "NEEDY" to 1))
            )),
            ShindanQuestion(12, "仕事と恋、どちらを優先する？", listOf(
                ShindanOption("断然、恋！", mapOf("NEEDY" to 2, "PASSION" to 1)),
                ShindanOption("基本は仕事だけど、大事な時は恋", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("どちらも選べない、両立させる", mapOf("LEADER" to 2, "PURE" to 1)),
                ShindanOption("その時の気分による", mapOf("FREE" to 2, "COOL" to 1))
            )),
            ShindanQuestion(13, "サプライズをされるのは好き？", listOf(
                ShindanOption("大好き！自分も仕掛けたい", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("少し照れるけど、嬉しい", mapOf("PURE" to 2, "HEALING" to 1)),
                ShindanOption("あまり好きではない、普通がいい", mapOf("COOL" to 2, "MATURE" to 1)),
                ShindanOption("反応に困るけど、喜んだふりをする", mapOf("STRATEGY" to 2, "DEVOTION" to 1))
            )),
            ShindanQuestion(14, "相手の過去の恋愛は気になる？", listOf(
                ShindanOption("全く気にならない、今が大事", mapOf("FREE" to 2, "MATURE" to 1)),
                ShindanOption("少し気になるけど、自分からは聞かない", mapOf("COOL" to 1, "HEALING" to 2)),
                ShindanOption("隠さず全部話してほしい", mapOf("LEADER" to 1, "NEEDY" to 2)),
                ShindanOption("自分と比較してしまいそうで怖い", mapOf("PURE" to 2, "DEVOTION" to 1))
            )),
            ShindanQuestion(15, "恋人に甘えるのと甘えられるの、どっち？", listOf(
                ShindanOption("全力で甘えたい", mapOf("NEEDY" to 2, "PURE" to 1)),
                ShindanOption("全力で甘やかしたい", mapOf("DEVOTION" to 2, "HEALING" to 1)),
                ShindanOption("お互いにバランスよく", mapOf("MATURE" to 2, "COOL" to 1)),
                ShindanOption("状況によって使い分けたい", mapOf("STRATEGY" to 2, "LEADER" to 1))
            )),
            ShindanQuestion(16, "理想の初デートの場所は？", listOf(
                ShindanOption("話題のカフェでゆっくりお喋り", mapOf("HEALING" to 1, "PURE" to 2)),
                ShindanOption("遊園地やテーマパークで騒ぎたい", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("映画館で定番デート", mapOf("DEVOTION" to 1, "COOL" to 1)),
                ShindanOption("水族館やプラネタリウムでロマンチックに", mapOf("MATURE" to 1, "STRATEGY" to 1))
            )),
            ShindanQuestion(17, "相手のどこに一番惹かれる？", listOf(
                ShindanOption("見た目のタイプさ", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("性格の良さや優しさ", mapOf("HEALING" to 2, "PURE" to 1)),
                ShindanOption("自分にはない才能や知識", mapOf("STRATEGY" to 1, "MATURE" to 2)),
                ShindanOption("一緒にいて楽な空気感", mapOf("COOL" to 2, "DEVOTION" to 1))
            )),
            ShindanQuestion(18, "恋人の誕生日はどう祝う？", listOf(
                ShindanOption("豪華なディナーと高級なプレゼント", mapOf("LEADER" to 2, "PASSION" to 1)),
                ShindanOption("手料理や手作り感のあるお祝いで", mapOf("DEVOTION" to 2, "PURE" to 1)),
                ShindanOption("相手が一番行きたいと言った場所へ連れて行く", mapOf("HEALING" to 2, "STRATEGY" to 1)),
                ShindanOption("さりげなく、でも記憶に残る演出を", mapOf("COOL" to 2, "MATURE" to 1))
            )),
            ShindanQuestion(19, "嫉妬心は強いほうだと思う？", listOf(
                ShindanOption("かなり強い、独占したい", mapOf("NEEDY" to 2, "PASSION" to 1)),
                ShindanOption("表には出さないけど、内心はモヤモヤする", mapOf("PURE" to 2, "DEVOTION" to 1)),
                ShindanOption("あまりない、相手を信頼している", mapOf("MATURE" to 2, "COOL" to 1)),
                ShindanOption("嫉妬させるようなことは自分もしない", mapOf("STRATEGY" to 2, "LEADER" to 1))
            )),
            ShindanQuestion(20, "遠距離恋愛になっても続けられる？", listOf(
                ShindanOption("絶対無理、毎日会いたい", mapOf("NEEDY" to 2, "PASSION" to 1)),
                ShindanOption("愛があれば乗り越えられる", mapOf("PURE" to 2, "DEVOTION" to 1)),
                ShindanOption("お互いの目標があるなら頑張れる", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("意外と一人の時間も楽しめて続くかも", mapOf("COOL" to 2, "FREE" to 1))
            )),
            ShindanQuestion(21, "恋人への不満、どう伝える？", listOf(
                ShindanOption("その場ですぐに言う", mapOf("PASSION" to 2, "LEADER" to 1)),
                ShindanOption("冷静に話し合う場を設ける", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("我慢して溜め込んでしまう", mapOf("DEVOTION" to 1, "PURE" to 2)),
                ShindanOption("冗談っぽく小出しにする", mapOf("HEALING" to 1, "COOL" to 2))
            )),
            ShindanQuestion(22, "一目惚れはするほう？", listOf(
                ShindanOption("よくする、直感を信じる", mapOf("PASSION" to 2, "FREE" to 1)),
                ShindanOption("たまにする、外見も大事", mapOf("PURE" to 1, "LEADER" to 1)),
                ShindanOption("あまりしない、中身を知ってから", mapOf("MATURE" to 2, "DEVOTION" to 1)),
                ShindanOption("論理的に分析してから好きになる", mapOf("STRATEGY" to 2, "COOL" to 1))
            )),
            ShindanQuestion(23, "SNSに恋人との写真を載せる？", listOf(
                ShindanOption("幸せをみんなに自慢したい", mapOf("PASSION" to 1, "LEADER" to 2)),
                ShindanOption("二人の思い出としてたまに載せる", mapOf("PURE" to 2, "HEALING" to 1)),
                ShindanOption("恥ずかしいので載せない", mapOf("COOL" to 2, "MATURE" to 1)),
                ShindanOption("相手の許可があれば載せる", mapOf("DEVOTION" to 2, "STRATEGY" to 1))
            )),
            ShindanQuestion(24, "恋人に嘘をつくことはある？", listOf(
                ShindanOption("絶対につかない", mapOf("PURE" to 2, "PASSION" to 1)),
                ShindanOption("相手を傷つけないための優しい嘘なら", mapOf("DEVOTION" to 2, "HEALING" to 1)),
                ShindanOption("面倒を避けるためにつくことがある", mapOf("FREE" to 2, "COOL" to 1)),
                ShindanOption("関係を円滑にするための戦略としてつく", mapOf("STRATEGY" to 2, "LEADER" to 1))
            )),
            ShindanQuestion(25, "理想の結婚時期は？", listOf(
                ShindanOption("できるだけ早くしたい", mapOf("NEEDY" to 2, "PURE" to 1)),
                ShindanOption("30歳前後が理想", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("特にこだわらない、タイミングが合えば", mapOf("FREE" to 2, "COOL" to 1)),
                ShindanOption("経済的に自立してから", mapOf("LEADER" to 2, "DEVOTION" to 1))
            )),
            ShindanQuestion(26, "恋人と趣味が合わない時、どうする？", listOf(
                ShindanOption("自分の趣味を一緒に楽しんでもらう", mapOf("LEADER" to 2, "PASSION" to 1)),
                ShindanOption("相手の趣味に自分も挑戦してみる", mapOf("DEVOTION" to 2, "HEALING" to 1)),
                ShindanOption("お互いに干渉せず別々に楽しむ", mapOf("COOL" to 2, "FREE" to 1)),
                ShindanOption("共通の新しい趣味を探す", mapOf("STRATEGY" to 2, "MATURE" to 1))
            )),
            ShindanQuestion(27, "復縁を迫られたらどうする？", listOf(
                ShindanOption("別れた理由が解決していればあり", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("一度終わったものは二度とない", mapOf("COOL" to 2, "PASSION" to 1)),
                ShindanOption("まだ好きなら迷わず戻る", mapOf("NEEDY" to 2, "PURE" to 2)),
                ShindanOption("友達としてなら付き合える", mapOf("FREE" to 2, "HEALING" to 1))
            )),
            ShindanQuestion(28, "自分からアプローチするほう？", listOf(
                ShindanOption("好きになったら自分からグイグイ行く", mapOf("PASSION" to 2, "LEADER" to 1)),
                ShindanOption("相手の様子を伺いつつ慎重に行く", mapOf("STRATEGY" to 2, "MATURE" to 1)),
                ShindanOption("相手から来てくれるのをひたすら待つ", mapOf("NEEDY" to 1, "PURE" to 2)),
                ShindanOption("共通の友人に協力してもらう", mapOf("DEVOTION" to 1, "HEALING" to 1))
            )),
            ShindanQuestion(29, "恋人と毎日電話したい？", listOf(
                ShindanOption("声を聞かないと眠れない", mapOf("NEEDY" to 2, "PASSION" to 1)),
                ShindanOption("時間があればしたい", mapOf("PURE" to 2, "HEALING" to 1)),
                ShindanOption("週末や特別な時だけでいい", mapOf("MATURE" to 2, "STRATEGY" to 1)),
                ShindanOption("文章（LINE）だけで十分", mapOf("COOL" to 2, "FREE" to 1))
            )),
            ShindanQuestion(30, "恋の前では自分を偽る？", listOf(
                ShindanOption("少しでも良く見せたいので偽る", mapOf("STRATEGY" to 2, "NEEDY" to 1)),
                ShindanOption("ありのままの自分を見てほしい", mapOf("FREE" to 2, "PURE" to 2)),
                ShindanOption("相手の好みに合わせようと努力する", mapOf("DEVOTION" to 2, "HEALING" to 1)),
                ShindanOption("無意識にカッコつけてしまう", mapOf("PASSION" to 1, "LEADER" to 2))
            ))
        ),
        results = listOf(
            ShindanResult("PASSION", "情熱的な直情ライオンタイプ", "燃え上がる恋の", "Passion Lion", "あなたは恋をすると一直線。ドラマチックな展開を好み、ライオンのように力強く相手を熱く愛します。", 85, mapOf("love" to 90, "loyalty" to 70, "security" to 50, "proactivity" to 95, "jealousy" to 80)),
            ShindanResult("DEVOTION", "尽くし上手なワンちゃんタイプ", "一途に支える", "Devoted Dog", "相手の幸せが自分の幸せ。忠犬のように一歩引いて相手を支え、献身的に尽くすことに喜びを感じる優しい人です。", 90, mapOf("love" to 95, "loyalty" to 95, "security" to 85, "proactivity" to 40, "jealousy" to 60)),
            ShindanResult("FREE", "自由を愛するハンター豹タイプ", "しなやかに駆ける", "Free Leopard", "束縛を嫌い、常にワクワクを求めるタイプ。豹のようにしなやかに、自分らしくいられる自由な関係を重視します。", 65, mapOf("love" to 60, "loyalty" to 50, "security" to 40, "proactivity" to 90, "jealousy" to 30)),
            ShindanResult("STRATEGY", "駆け引き上手のキツネタイプ", "冷静に心を射抜く", "Strategic Fox", "感情に流されず、どうすれば関係がうまくいくかを分析する知性派。キツネのように賢く立ち回り、恋の主導権を握ります。", 70, mapOf("love" to 50, "loyalty" to 60, "security" to 70, "proactivity" to 75, "jealousy" to 65)),
            ShindanResult("PURE", "純粋無垢な白うさぎタイプ", "真っ白な愛を育む", "Pure Rabbit", "初恋のような純粋な気持ちを大切にします。うさぎのように寂しがり屋で一途。嘘が苦手で、誠実な愛を育みます。", 95, mapOf("love" to 85, "loyalty" to 95, "security" to 80, "proactivity" to 45, "jealousy" to 50)),
            ShindanResult("MATURE", "どっしり構えたゾウさんタイプ", "大きな愛で包む", "Mature Elephant", "落ち着いた安定感のある関係を好みます。ゾウさんのような大きな心で相手を包み込み、精神的な繋がりを何よりも大切にします。", 80, mapOf("love" to 90, "loyalty" to 85, "security" to 95, "proactivity" to 30, "jealousy" to 20)),
            ShindanResult("LEADER", "威厳あるリーダータイガータイプ", "二人を導く", "Leader Tiger", "恋愛でも主導権を握り、相手をリードしていくことに長けています。虎のような圧倒的な頼もしさで、二人を引っ張っていきます。", 75, mapOf("love" to 75, "loyalty" to 80, "security" to 90, "proactivity" to 95, "jealousy" to 50)),
            ShindanResult("HEALING", "ゆるふわ癒やしのパンダタイプ", "穏やかな時を贈る", "Healing Panda", "一緒にいるだけで相手をリラックスさせる不思議な魅力の持ち主。パンダのように愛くるしく、穏やかな時間を提供します。", 85, mapOf("love" to 85, "loyalty" to 80, "security" to 95, "proactivity" to 20, "jealousy" to 15)),
            ShindanResult("COOL", "クールで賢い黒猫タイプ", "気まぐれに魅了する", "Cool Black Cat", "恋愛が生活の全てではなく、自分の時間も大切にするスマートなタイプ。黒猫のように気まぐれでミステリアスな魅力があります。", 60, mapOf("love" to 50, "loyalty" to 70, "security" to 60, "proactivity" to 60, "jealousy" to 25)),
            ShindanResult("NEEDY", "かまってちゃんのリスタイプ", "愛情を欲しがる", "Needy Squirrel", "常に繋がっていたい、甘えん坊な一面があります。リスのようにちょこちょこ動き回り、深い愛情を求め、また与えます。", 80, mapOf("love" to 95, "loyalty" to 90, "security" to 40, "proactivity" to 70, "jealousy" to 95))
        )
    )
}
