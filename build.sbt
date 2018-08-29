import AssemblyKeys._ 


name := "segm"

version := "0.1"

scalaVersion := "2.10.3"


fork := true


assemblySettings


unmanagedJars in Compile ++= List(file("lib/"))

unmanagedSourceDirectories in Compile := List(file("src/"))

target in Compile := file("target/")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case x => MergeStrategy.first
  }
}