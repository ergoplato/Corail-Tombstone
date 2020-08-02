package ovh.corail.tombstone.helper;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HTTPUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

/*
	Made by Paul Fulham for Corail Tombstone
	https://gist.github.com/pau101/e1e04bf58e81930548ab6c3671f8bee7
*/
public final class ContributorStore {
    private static final long DEFAULT_DOWNLOAD_LIMIT = 1024;
    private final ImmutableSet<UUID> uuids;

    private ContributorStore(final ImmutableSet<UUID> uuids) {
        this.uuids = uuids;
    }

    public boolean contains(final PlayerEntity player) {
        return this.contains(PlayerEntity.getUUID(player.getGameProfile()));
    }

    public boolean contains(final UUID uuid) {
        return this.uuids.contains(uuid);
    }

    public boolean contains(final String uuidString) {
        return this.uuids.stream().anyMatch(p -> p.toString().equals(uuidString));
    }

    public boolean isEmpty() {
        return this.uuids.isEmpty();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof ContributorStore && this.uuids.equals(((ContributorStore) obj).uuids);
    }

    @Override
    public int hashCode() {
        return this.uuids.hashCode();
    }

    @Override
    public String toString() {
        return "ContributorStore{uuids=" + this.uuids + '}';
    }

    public static ListenableFuture<ContributorStore> read(final URL url, final Proxy proxy) {
        return ContributorStore.read(url, proxy, DEFAULT_DOWNLOAD_LIMIT);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static ListenableFuture<ContributorStore> read(final URL url, final Proxy proxy, final long downloadLimit) {
        return HTTPUtil.DOWNLOADER_EXECUTOR.submit(() -> {
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setInstanceFollowRedirects(true);
            final ImmutableSet.Builder<UUID> uuids = ImmutableSet.builder();
            try (final JsonReader reader = new JsonReader(
                    new InputStreamReader(
                            com.google.common.io.ByteStreams.limit(conn.getInputStream(), downloadLimit),
                            ContributorStore.getContentEncoding(conn.getContentType())
                    )
            )) {
                reader.beginArray();
                while (reader.hasNext()) {
                    uuids.add(UUID.fromString(reader.nextString()));
                }
                reader.endArray();
            }
            return new ContributorStore(uuids.build());
        });
    }

    private static Charset getContentEncoding(final String contentType) {
        final String charsetEquals = "charset=";
        final int charsetIndex = StringUtils.indexOfIgnoreCase(contentType, charsetEquals);
        if (charsetIndex != StringUtils.INDEX_NOT_FOUND) {
            final String encoding = contentType.substring(charsetIndex + charsetEquals.length());
            try {
                return Charset.forName(encoding);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Charsets.UTF_8;
    }

    public static ContributorStore of() {
        return new ContributorStore(ImmutableSet.of());
    }
}
