# WebInventory

データベースを使ったインベントリを追加します。

身内用なので汎用性は皆無です。

たぶん不具合だらけなので使わない方がいいと思います。

Spigot-1.8.8で動作テストしています。

## 使い方(How to use)

### OP

config.ymlで使用するデータベースの設定をしてください。

adminコマンドでいろいろできます。

### プレイヤー

`/wi signup`コマンドで登録するとインベントリを使用できるようになります。

インベントリを開くには権限をもった人(もしくはサーバー)にopenコマンドを実行してもらう必要があります。

## コマンド(Commands)

### admin
command: `webinventoryadmin`

aliases: [`wiadmin`, `wia`]

`/wia <toggle | close | reconnect | reload | yaml>`

* toggle
  - WebInventoryが開けるか開けないかを切り替える
  - 開けなくなると同時に開かれているすべてのWebInventoryを閉じる
* close
  - 開かれているすべてのWebInventoryを閉じる
  - データベースを直接いじるときに使うコマンドです
* reconnect
  - (configを再読み込みして)データベースに再接続する
* reload
  - messages.ymlを再読み込みする
* yaml
  - 未実装です❤


### open
command: `webinventoryopen`

aliases: [`wiopen`, `wio`]

`/wio <Player>`

指定したプレイヤーのインベントリを開かせる

### view
command: `webinventoryview`

aliases: [`wiview`, `wiv`]

`/wiv <Player>`

指定したプレイヤーのインベントリを覗く

### wi
command: `webinventory`

aliases: [`webi`, `wi`]

`/wi signup`

登録する

`/wi`

自分のインベントリを開く

## パーミッション(Permissions)

* webinventory.*
  - WebInventoryの全権限
  - デフォルト: op
* webinventory.command.admin
  - wiaコマンドの権限
  - デフォルト: op
* webinventory.command.open
  - wioコマンドの権限
  - デフォルト: op
* webinventory.command.view
  - wivコマンドの権限
  - デフォルト: op
* webinventory.command.wi
  - wiコマンドの権限
  - デフォルト: true
* webinventory.use
  - WebInventoryを使う権限
  - デフォルト: true


## 著者(Author)
**Reona Oshima (totoraj)**
* [http://totoraj.net](http://totoraj.net/)
* [Twitter: @totoraj930](https://twitter.com/totoraj930/)


## ライセンス(License)
Copyright &copy; 2016 Reona Oshima (totoraj)
This work is released  under the MIT License.
<http://opensource.org/licenses/mit-license.php>

