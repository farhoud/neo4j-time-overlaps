package interval;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.harness.junit.Neo4jRule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IntervalOverlapsTest
{
    // This rule starts a Neo4j instance
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()

            // This is the function we want to test
            .withFunction( IntervalOverlaps.class );

    @Test
    public void shouldAllowIndexingAndFindingANode() throws Throwable
    {
        // This is in a try-block, to make sure we close the driver after the test
        try( Driver driver = GraphDatabase
                .driver( neo4j.boltURI() , Config.build().withEncryptionLevel( Config.EncryptionLevel.NONE ).toConfig() ) )
        {
            // Given
            Session session = driver.session();

            // When
            boolean result = session.run( "RETURN interval.overlaps(localdatetime('2019-01-01T00:00:01'),localdatetime('2019-02-01T00:00:01'),localdatetime('2019-01-01T00:00:01'),localdatetime('2019-01-04T00:00:01')) AS result").single().get("result").asBoolean();

            // Then
            assertThat( result, equalTo( true ) );

            // When
            Long result2 = session.run( "RETURN interval.intersectInMinutes(localdatetime('2019-01-01T00:00:01'),localdatetime('2019-01-04T00:00:01'),localdatetime('2019-01-03T00:00:01'),localdatetime('2019-01-04T00:00:01')) AS result").single().get("result").asLong();

            // Then

            // When
            Long result4 = session.run( "RETURN interval.intersectInMinutes(localdatetime('2019-01-01T00:00:01'),localdatetime('2019-01-04T00:00:01'),localdatetime('2019-01-03T00:00:01'),localdatetime('2019-01-04T00:00:01')) AS result").single().get("result").asLong();

            // Then
            assertThat( result4, equalTo( 1440L ) );

            // When
            Long result3 = session.run( "RETURN interval.intersectInMinutes(localdatetime('2019-01-01T00:00:01'),localdatetime('2019-01-02T00:00:01'),localdatetime('2019-01-10T00:00:01'),localdatetime('2019-01-15T00:00:01')) AS result").single().get("result").asLong();

            // Then
            assertThat( result3, equalTo( 0L ) );
        }
    }
}