package com.dempe.ocean.rpc.transport.protocol;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class PacketData {

    private static Logger LOG = LoggerFactory.getLogger(PacketData.class);

    private HeadMeta head;
    private RpcMeta rpcMeta;
    private byte[] data;
    private byte[] attachment;

    private long timeStamp;

    public void mergeData(byte[] data) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = data;
            return;
        }

        int len = this.data.length + data.length;
        byte[] newData = new byte[len];
        System.arraycopy(this.data, 0, newData, 0, this.data.length);
        System.arraycopy(data, 0, newData, this.data.length, data.length);
        this.data = newData;
    }

    public boolean isChunkPackage() {
        return getChunkStreamId() != null;
    }

    public boolean isFinalPackage() {
        if (rpcMeta == null) {
            return true;
        }
        ChunkInfo chunkInfo = rpcMeta.getChunkInfo();
        if (chunkInfo == null) {
            return true;
        }

        return chunkInfo.getChunkId() == -1;
    }

    public Long getChunkStreamId() {
        if (rpcMeta == null) {
            return null;
        }
        ChunkInfo chunkInfo = rpcMeta.getChunkInfo();
        if (chunkInfo == null) {
            return null;
        }

        return chunkInfo.getStreamId();
    }

    /**
     * To split current {@link PacketData} by chunkSize. if chunkSize great than data length will not do split.<br>
     * {@link java.util.List} return will never be {@code null} or empty.
     *
     * @param chunkSize target size to split
     * @return {@link java.util.List} of {@link PacketData} after split
     */
    public List<PacketData> chunk(long chunkSize) {
        if (chunkSize < 1 || data == null || chunkSize > data.length) {
            return Arrays.asList(this);
        }

        long streamId = UUID.randomUUID().toString().hashCode();
        int chunkId = 0;
        int startPos = 0;

        int cSize = Long.valueOf(chunkSize).intValue();
        List<PacketData> ret = new ArrayList<PacketData>();
        while (startPos < data.length) {
            byte[] subArray = ArrayUtils.subarray(data, startPos, startPos + cSize);
            PacketData clone = copy();
            clone.data(subArray);
            if (startPos > 0) {
                clone.attachment(null);
            }
            startPos += cSize;
            if (startPos >= data.length) {
                chunkId = -1;
            }
            clone.chunkInfo(streamId, chunkId);
            ret.add(clone);
            chunkId++;

        }

        return ret;
    }

    public PacketData copy() {
        PacketData rpcDataPackage = new PacketData();
        if (head != null) {
            rpcDataPackage.setHead(head.copy());
        }

        if (rpcMeta != null) {
            rpcDataPackage.setRpcMeta(rpcMeta.copy());
        }

        rpcDataPackage.setData(data);
        rpcDataPackage.setAttachment(attachment);

        return rpcDataPackage;
    }

    /**
     * set magic code
     *
     * @param magicCode
     * @return
     */
    public PacketData magicCode(String magicCode) {
        setMagicCode(magicCode);
        return this;
    }

    /**
     * @param serviceName
     * @return
     */
    public PacketData serviceName(String serviceName) {
        RequestMeta request = initRequest();
        request.setServiceName(serviceName);
        return this;
    }

    public PacketData methodName(String methodName) {
        RequestMeta request = initRequest();
        request.setMethodName(methodName);
        return this;
    }

    public PacketData data(byte[] data) {
        setData(data);
        return this;
    }

    public PacketData attachment(byte[] attachment) {
        setAttachment(attachment);
        return this;
    }

    public PacketData authenticationData(byte[] authenticationData) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setAuthenticationData(authenticationData);
        return this;
    }

    public PacketData correlationId(long correlationId) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setCorrelationId(correlationId);
        return this;
    }

    public PacketData compressType(int compressType) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setCompressType(compressType);
        return this;
    }

    public PacketData errorCode(int errorCode) {
        ResponseMeta response = initResponse();
        response.setErrorCode(errorCode);
        return this;
    }

    public PacketData errorText(String errorText) {
        ResponseMeta response = initResponse();
        response.setErrorText(errorText);
        return this;
    }

    public PacketData extraParams(byte[] params) {
        RequestMeta request = initRequest();
        request.setExtraParam(params);
        return this;
    }

    public PacketData chunkInfo(long streamId, int chunkId) {
        ChunkInfo chunkInfo = new ChunkInfo();
        chunkInfo.setStreamId(streamId);
        chunkInfo.setChunkId(chunkId);
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setChunkInfo(chunkInfo);
        return this;
    }

    /**
     *
     */
    private RequestMeta initRequest() {
        RpcMeta rpcMeta = initRpcMeta();

        RequestMeta request = rpcMeta.getRequest();
        if (request == null) {
            request = new RequestMeta();
            rpcMeta.setRequest(request);
        }

        return request;
    }

    private ResponseMeta initResponse() {
        RpcMeta rpcMeta = initRpcMeta();

        ResponseMeta response = rpcMeta.getResponse();
        if (response == null) {
            response = new ResponseMeta();
            rpcMeta.setResponse(response);
        }

        return response;
    }

    /**
     *
     */
    private RpcMeta initRpcMeta() {
        if (rpcMeta == null) {
            rpcMeta = new RpcMeta();
        }
        return rpcMeta;
    }

    public void setMagicCode(String magicCode) {
        if (head == null) {
            head = new HeadMeta();
        }
        head.setMagicCode(magicCode);
    }

    /**
     * get the head
     *
     * @return the head
     */
    public HeadMeta getHead() {
        return head;
    }

    /**
     * set head value to head
     *
     * @param head the head to set
     */
    public void setHead(HeadMeta head) {
        this.head = head;
    }

    /**
     * get the rpcMeta
     *
     * @return the rpcMeta
     */
    public RpcMeta getRpcMeta() {
        return rpcMeta;
    }

    /**
     * set rpcMeta value to rpcMeta
     *
     * @param rpcMeta the rpcMeta to set
     */
    public void setRpcMeta(RpcMeta rpcMeta) {
        this.rpcMeta = rpcMeta;
    }

    /**
     * get the data
     *
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * set data value to data
     *
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * get the timeStamp
     *
     * @return the timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * set timeStamp value to timeStamp
     *
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * get the attachment
     *
     * @return the attachment
     */
    public byte[] getAttachment() {
        return attachment;
    }

    /**
     * set attachment value to attachment
     *
     * @param attachment the attachment to set
     */
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public PacketData getErrorResponseRpcDataPackage(int errorCode, String errorText) {
        return getErrorResponseRpcDataPackage(new ResponseMeta(errorCode, errorText));
    }

    public PacketData getErrorResponseRpcDataPackage(ResponseMeta responseMeta) {
        PacketData response = new PacketData();

        RpcMeta eRpcMeta = rpcMeta;
        if (eRpcMeta == null) {
            eRpcMeta = new RpcMeta();
        }
        eRpcMeta.setResponse(responseMeta);
        eRpcMeta.setRequest(null);

        response.setRpcMeta(eRpcMeta);
        response.setHead(head);

        return response;
    }


    public byte[] write() {
        if (head == null) {
            throw new RuntimeException("property 'head' is null.");
        }
        if (rpcMeta == null) {
            throw new RuntimeException("property 'rpcMeta' is null.");
        }

        int totolSize = 0;

        // set dataSize
        int dataSize = 0;
        if (data != null) {
            dataSize = data.length;
            totolSize += dataSize;
        }

        // set attachment size
        int attachmentSize = 0;
        if (attachment != null) {
            attachmentSize = attachment.length;
            totolSize += attachmentSize;
        }
        rpcMeta.setAttachmentSize(attachmentSize);

        // get RPC meta data
        byte[] rpcMetaBytes = rpcMeta.write();
        int rpcMetaSize = rpcMetaBytes.length;
        totolSize += rpcMetaSize;
        head.setMetaSize(rpcMetaSize);
        head.setMsgSize(totolSize); // set message body size

        // total size should add head size
        totolSize = totolSize + HeadMeta.SIZE;
        try {
            // join all byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream(totolSize);
            // write head
            baos.write(head.write());

            // write meta data
            baos.write(rpcMetaBytes);

            // write data
            if (data != null) {
                baos.write(data);
            }

            if (attachment != null) {
                baos.write(attachment);
            }

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    public void read(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("param 'bytes' is null.");
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        // read head data
        byte[] headBytes = new byte[HeadMeta.SIZE];
        bais.read(headBytes, 0, HeadMeta.SIZE);

        // parse RPC head
        head = new HeadMeta();
        head.read(headBytes);

        // get RPC meta size
        int metaSize = head.getMetaSize();
        // read meta data
        byte[] metaBytes = new byte[metaSize];
        bais.read(metaBytes, 0, metaSize);

        rpcMeta = new RpcMeta();
        rpcMeta.read(metaBytes);

        int attachmentSize = rpcMeta.getAttachmentSize();

        // read message data
        // message data size = totalsize - metasize - attachmentSize
        int totalSize = head.getMsgSize();
        int dataSize = totalSize - metaSize - attachmentSize;

        if (dataSize > 0) {
            data = new byte[dataSize];
            bais.read(data, 0, dataSize);
        }

        // if need read attachment
        if (attachmentSize > 0) {
            attachment = new byte[attachmentSize];
            bais.read(attachment, 0, attachmentSize);
        }

    }

    @Override
    public String toString() {
        return "PacketData{" +
                "head=" + head +
                ", rpcMeta=" + rpcMeta +
                ", data=" + Arrays.toString(data) +
                ", attachment=" + Arrays.toString(attachment) +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
