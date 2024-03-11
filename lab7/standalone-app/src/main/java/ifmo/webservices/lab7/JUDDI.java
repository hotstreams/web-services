package ifmo.webservices.lab7;

import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;
import org.uddi.api_v3.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class JUDDI {
    private final UDDISecurityPortType security;
    private final UDDIInquiryPortType inquiry;
    private final UDDIPublicationPortType publish;

    private final String authToken;

    public JUDDI(final String user, final String password) {
        try {
            UDDIClient client = new UDDIClient("META-INF/juddi.xml");
            Transport transport = client.getTransport("default");
            security = transport.getUDDISecurityService();
            inquiry = transport.getUDDIInquiryService();
            publish = transport.getUDDIPublishService();
            GetAuthToken getAuthToken = new GetAuthToken();
            getAuthToken.setUserID(user);
            getAuthToken.setCred(password);
            authToken = security.getAuthToken(getAuthToken).getAuthInfo();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void registerService(String businessName, String serviceName,  String serviceURL) {
        try {
            BusinessEntity businessEntity = new BusinessEntity();
            businessEntity.getName().add(new Name(businessName, "en"));

            SaveBusiness saveBusinessOperation = new SaveBusiness();
            saveBusinessOperation.getBusinessEntity().add(businessEntity);
            saveBusinessOperation.setAuthInfo(authToken);
            BusinessDetail businessDetail = publish.saveBusiness(saveBusinessOperation);
            String businessKey = businessDetail.getBusinessEntity().get(0).getBusinessKey();

            BusinessService service = new BusinessService();
            service.setBusinessKey(businessKey);
            service.getName().add(new Name(serviceName, "en"));

            BindingTemplate bindingTemplate = new BindingTemplate();
            bindingTemplate.setAccessPoint(new AccessPoint(serviceURL, AccessPointType.WSDL_DEPLOYMENT.toString()));

            BindingTemplates bindingTemplates = new BindingTemplates();
            bindingTemplate = UDDIClient.addSOAPtModels(bindingTemplate);
            bindingTemplates.getBindingTemplate().add(bindingTemplate);
            service.setBindingTemplates(bindingTemplates);

            SaveService saveService = new SaveService();
            saveService.getBusinessService().add(service);
            saveService.setAuthInfo(authToken);

            publish.saveService(saveService);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public URL findServiceURL(String serviceName) {
        try {
            FindService findService = new FindService();
            findService.setAuthInfo(authToken);
            findService.setFindQualifiers(new FindQualifiers());
            findService.getFindQualifiers().getFindQualifier().add(UDDIConstants.EXACT_MATCH);

            Name name = new Name();
            name.setValue(serviceName);
            findService.getName().add(name);

            ServiceList serviceList = inquiry.findService(findService);
            if (serviceList.getServiceInfos() == null) {
                throw new NoSuchServiceException();
            }
            ServiceInfo serviceInfo = serviceList.getServiceInfos().getServiceInfo().get(0);
            GetServiceDetail getServiceDetail = new GetServiceDetail();
            getServiceDetail.getServiceKey().add(serviceInfo.getServiceKey());
            ServiceDetail serviceDetail = inquiry.getServiceDetail(getServiceDetail);
            BusinessService service = serviceDetail.getBusinessService().get(0);
            String accessPoint = service.getBindingTemplates().getBindingTemplate().get(0).getAccessPoint().getValue();
            return new URL(accessPoint);
        } catch (RemoteException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
