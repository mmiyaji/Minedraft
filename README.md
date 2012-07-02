Minedraft
---------------
## これなん
Java製対戦ゲームっぽい何かです．
本プロジェクトでは講義で学んだことを生かして，対戦ゲームのAI（人工知能）を作成します．
単純に与えられたことをこなす課題ではなく，制約条件の中でどうすればAIが強くなるかを考え，それを自ら実装することでプログラミング力だけでなく自由な発想を養って欲しいと考えています．
講義が進むに連れて，ゲームの制約条件を増やしていき，より高度な環境でAIの思考パターンを作成してもらう予定です．
## 内容
アクションシューティング型AI対戦ゲームです．
障害物のあるフィールド上で，キャラクター同士が玉を投げ合い，当てた玉の数を競う単純なゲームです．フィールドや障害物などのAI部分以外の実装はTAが行っています．そのため，皆さんにはAIファイルだけを変更することでプレーヤーの振る舞いをプログラミングしてもらいます．その際に必要なフィールドなどのデータを取得するAPIは用意してあります（詳細はminedraft_manual.pdf）．

### ルール
- 長方形のマス上のフィールドに障害物・プレーヤーを配置
- 外周には必ず壁（障害物）が配置，一列に一つランダムで障害物が配置される
- すべてのプレーヤーが交代で行動する
- 一回の行動でプレーヤーは 移動・方向転換・玉を投げる 動作をすることができる
- 一回の行動中で玉を投げる動作は一回まで．その他は好きに使える
- 玉を投げると何かに当たるまで玉は飛び続ける（自然消滅しない）．自分には当たらないが，味方（同じグループに所属しているプレーヤー）には当たる
- 一人のプレーヤーが行動を終了して，次のプレーヤーに行動権利が移るまでを1ターンとする（行動中に玉を投げた場合，玉が着弾するまで次のターンに移らない）
- 終了条件は指定したターン数を経過したとき
- 玉を他プレーヤーに当てた場合，加点．逆に被弾した場合，減点
- プレーヤーはグループで分けられており，グループ間での総得点で勝敗を決める

## 課題内容
AI部分の作成を行ってもらいます．
詳細はminedraft_manual.pdf に記載
![Minedraft](https://github.com/mmiyaji/Minedraft/raw/master/images/screenshot2.png)

# License
packege game:
The MIT License


Copyright (c) 2012 Masahiro MIYAJI


Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

以下に定める条件に従い、本ソフトウェアおよび関連文書のファイル（以下「ソフトウェア」）の複製を取得するすべての人に対し、ソフトウェアを無制限に扱うことを無償で許可します。これには、ソフトウェアの複製を使用、複写、変更、結合、掲載、頒布、サブライセンス、および/または販売する権利、およびソフトウェアを提供する相手に同じことを許可する権利も無制限に含まれます。

上記の著作権表示および本許諾表示を、ソフトウェアのすべての複製または重要な部分に記載するものとします。

ソフトウェアは「現状のまま」で、明示であるか暗黙であるかを問わず、何らの保証もなく提供されます。ここでいう保証とは、商品性、特定の目的への適合性、および権利非侵害についての保証も含みますが、それに限定されるものではありません。 作者または著作権者は、契約行為、不法行為、またはそれ以外であろうと、ソフトウェアに起因または関連し、あるいはソフトウェアの使用またはその他の扱いによって生じる一切の請求、損害、その他の義務について何らの責任も負わないものとします。

package gui:
 forked from OskarVeerhoek/YouTube-tutorials episode-19.
 Read LICENCE_CODE.txt
