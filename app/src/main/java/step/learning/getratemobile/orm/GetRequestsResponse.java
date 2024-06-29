package step.learning.getratemobile.orm;

import org.json.JSONObject;

public class GetRequestsResponse {
    private String Status;
    private GetRequestsData Data;

    public static GetRequestsResponse fromJsonString( String jsonString ) {
        try{
            JSONObject root = new JSONObject( jsonString );
            String status = root.getString( "status" );

            JSONObject data = root.getJSONObject( "data" );
            GetRequestsData getRequestsData = GetRequestsData.fromJsonString( data );

            GetRequestsResponse res = new GetRequestsResponse();
            res.setStatus( status );
            res.setData( getRequestsData );

            return res;
        }
        catch ( Exception ex ) {
            throw new IllegalArgumentException( ex.getMessage() );
        }
    }
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public GetRequestsData getData() {
        return Data;
    }

    public void setData(GetRequestsData data) {
        Data = data;
    }
}
