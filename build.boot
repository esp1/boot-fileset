(set-env!
  :resource-paths #{"src"}
  :dependencies '[[adzerk/bootlaces "0.1.13"]])

(def +version+ "0.1.0-SNAPSHOT")

(task-options!
  pom {:project 'mecha1/boot-fileset
       :version +version+})

(require '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)