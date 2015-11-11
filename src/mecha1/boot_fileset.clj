(ns mecha1.boot-fileset
  (:require [boot.core :as boot]
            [boot.util :as util]
            [clojure.java.io :as io]))

(boot/deftask gen->fs
  "Add generated files to the fileset. The generator functions should return one or more file specs.
  Each file spec is a map with keys :path, :fs, and :content, where path is a path string relative to the fileset root,
  fs is one of :source, :resource, or :asset indicating which component of the fileset the file should be associated with,
  and contnent is the string content of the file."
  [g gen FNS sym "File spec generator function."]
  (let [source-dir   (boot/tmp-dir!)
        resource-dir (boot/tmp-dir!)
        asset-dir    (boot/tmp-dir!)]
    (boot/with-pre-wrap fileset
      (if-let [pages-fn (resolve gen)]
        (doseq [{:keys [path fs content]} (pages-fn)]
          (let [fs-dir (condp = fs
                         :source source-dir
                         :resource resource-dir
                         :asset asset-dir)
                f (io/file fs-dir path)]
            (.mkdirs (io/file (.getParent f)))
            (spit f content)))
        (util/fail "Unable to resolve %s\n" gen))
      (-> fileset
        (boot/add-source source-dir)
        (boot/add-resource resource-dir)
        (boot/add-asset asset-dir)
        boot/commit!))))
