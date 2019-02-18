(ns ^:figwheel-no-load scramblies-challenge.dev
  (:require
    [scramblies-challenge.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
