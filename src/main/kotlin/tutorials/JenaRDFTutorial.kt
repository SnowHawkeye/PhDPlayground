/*
Tutorial link: https://jena.apache.org/tutorials/rdf_api.html
 */
package tutorials

import org.apache.jena.rdf.model.*
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.vocabulary.RDFS
import org.apache.jena.vocabulary.VCARD
import java.io.File

fun main(args: Array<String>) {

    val model = createSimpleGraph() // make simple model describing a person
    section("STATEMENTS"); printModelStatements(model) // print statements from the graph
    section("WRITE MODEL"); writeModel(model) // print model in XML and JSON form
    section("READ MODEL"); readModel() // print a model read from an external RDF file
    section("PREFIXES"); controlPrefixes()
    section("QUERY"); navigateModel()
    section("MERGE"); mergeModels()
    section("LITERALS"); createAdvancedLiterals()

}

fun section(text: String = "") = println("\n$text _____________________________________________\n")

private fun createSimpleGraph(): Model {
    val personURI = "http://somewhere/JohnSmith"
    val givenName = "John"
    val familyName = "Smith"
    val fullName = "$givenName $familyName"

    // Model is an interface representing a graph

    // create an empty Model
    val model = ModelFactory.createDefaultModel()

    // create the resource and add the property
    val johnSmith: Resource = model.createResource(personURI)
        .addProperty(VCARD.FN, fullName)
        .addProperty(
            VCARD.N, model.createResource() // blank node
                .addProperty(VCARD.Given, givenName)
                .addProperty(VCARD.Family, familyName)
        )
    return model
}

private fun printModelStatements(model: Model) {
    val iterator = model.listStatements()
    iterator.forEach {
        println("${it.subject} ${it.predicate} ${it.`object`}")
    }
}

private fun writeModel(model: Model) {
    model.write(System.out) // writes the model "naively" in XML form, but does not support things like blank nodes
    section()
    RDFDataMgr.write(System.out, model, Lang.RDFJSON) // different writer that allows the use of different conventions
}

fun readModel(): Model {
    val model = ModelFactory.createDefaultModel() // create empty model to host the read model
    val inputFileName = "src/main/kotlin/tutorials/jena-tutorial-database.rdf"

    File(inputFileName).forEachLine { println(it) }

    val inputStream = RDFDataMgr.open(inputFileName)
        ?: throw IllegalArgumentException("File $inputFileName not found.")
    model.read(inputStream, null)
    //model.write(System.out)
    inputStream.close()
    return model
}

fun controlPrefixes() {
    val m = ModelFactory.createDefaultModel()
    val namespaceA = "http://somewhere/else#"
    val namespaceB = "http://nowhere/else#"
    val root = m.createResource(namespaceA + "root") // resource
    val propertyP: Property = m.createProperty(namespaceA + "P")
    val propertyQ: Property = m.createProperty(namespaceB + "Q")
    val x = m.createResource(namespaceA + "x")
    val y = m.createResource(namespaceA + "y")
    val z = m.createResource(namespaceA + "z")

    m.add(root, propertyP, x) // add(resource, property, node), adds statement to the model
        .add(root, propertyQ, y)
        .add(y, propertyQ, z)

    println("# -- no special prefixes defined")
    m.write(System.out)
    println("# -- namespaceA defined")
    m.setNsPrefix("namespaceA", namespaceA)
    m.write(System.out)
    println("# -- namespaceA and cat defined")
    m.setNsPrefix("cat", namespaceB)
    m.write(System.out)
}

fun navigateModel() {
    val model = readModel()

    // to access a property with this method, you need to know the resource URI
    val johnSmithURI = "http://somewhere/JohnSmith"
    val vcard = model.getResource(johnSmithURI)

    /*
    getProperty returns a statement, and we then access the object of said statement
    ⚠️ if the vcard has repeat properties (e.g. several nicknames),
    the getProperty function will inconsistently return one of them.
    `Resource.listProperties(p: Property)` lists them all.
     */
    val nameResource = vcard.getProperty(VCARD.N).resource
    // val nameString = vcard.getProperty(VCARD.N).string

    val nullString: String? = null // used to circumvent an overload error
    model.listStatements(null, VCARD.FN, nullString) // nulls indicate any value should be accepted
        .apply { if (this.hasNext()) println("The database contains vcards for:") }
        .forEach { println(it.string) }

    class TestSelector(r: Resource?, p: Property?, s: String?) : SimpleSelector(r, p, s) {
        override fun selects(s: Statement?): Boolean = s?.string?.endsWith("Smith") ?: false
    }

    model.listStatements(TestSelector(null, VCARD.FN, null))
        .apply { if (this.hasNext()) println("The database contains vcards ending with 'Smith' for:") }
        .forEach { println(it.string) }
}

fun mergeModels() {
    val model1 = readModel()
    val model2 = readModel()

    val model = model1.union(model2)
    RDFDataMgr.write(System.out, model, Lang.RDFXML)
}

fun createAdvancedLiterals() {
    val model = ModelFactory.createDefaultModel()
    val r = model.createResource()
    r.addProperty(RDFS.label, model.createLiteral("chat", "en"))
    r.addProperty(RDFS.label, model.createLiteral("chat", "fr"))
    r.addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true))
    model.write(System.out)
}


