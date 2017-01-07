(defproject tic-tac-toe "0.1.0-SNAPSHOT"
  :description "Implementing the classic children's game using Clojure."
  :url "https://github.com/av-pinzur/tictactoe-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot tic-tac-toe.ui.console
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
