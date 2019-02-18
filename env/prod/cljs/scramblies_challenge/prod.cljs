(ns scramblies-challenge.prod
  (:require [scramblies-challenge.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
