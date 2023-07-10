package org.alice.ide.project.codecs;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.projecturi.ProjectSnapshot;
import org.lgna.croquet.ItemCodec;

import java.net.URI;

// Codec writes and reads only the URL to identify the file
public enum ProjectSnapshotCodec implements ItemCodec<ProjectSnapshot> {
  SINGLETON;

  @Override
  public ProjectSnapshot decodeValue(BinaryDecoder binaryDecoder) {
    boolean isNotNull = binaryDecoder.decodeBoolean();
    if (isNotNull) {
      return new ProjectSnapshot(URI.create(binaryDecoder.decodeString()));
    }
    return null;
  }

  @Override
  public void encodeValue(BinaryEncoder binaryEncoder, ProjectSnapshot value) {
    boolean hasValue = value != null && value.hasUri();
    binaryEncoder.encode(hasValue);
    if (hasValue) {
      binaryEncoder.encode(value.getUri().toString());
    }
  }

  @Override
  public Class<ProjectSnapshot> getValueClass() {
    return ProjectSnapshot.class;
  }

  @Override
  public void appendRepresentation(StringBuilder sb, ProjectSnapshot value) {
    sb.append(value);
  }
}
