import grails.test.AbstractCliTestCase

class RestApiDocTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRestApiDoc() {

        execute(["rest-api-doc"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
