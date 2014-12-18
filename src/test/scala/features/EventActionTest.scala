package features

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.github.nicholasren.moco.helper.RemoteTestHelper
import com.github.nicholasren.moco.dsl.{Conversions, SMoco}
import com.github.nicholasren.moco.dsl.SMoco._
import com.github.dreamhead.moco.MocoEventAction
import org.scalatest.mock.MockitoSugar
import Conversions._
import org.mockito.Mockito._

class EventActionTest extends FunSpec with BeforeAndAfter with RemoteTestHelper with MockitoSugar {
  override val port: Int = 8083

  var theServer: SMoco = null

  before {
    theServer = server(port)
  }

  describe("on complete") {

    it("perform predefined action") {

      val action = mock[MocoEventAction]

      theServer default {
        text("foo")
      } on {
        complete(action)
      }

      theServer running {
        assert(get(root) === "foo")
      }

      verify(action).execute

    }

  }

}
